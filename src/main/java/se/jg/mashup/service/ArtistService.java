package se.jg.mashup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.jg.mashup.dto.Album;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.integration.artist.ArtistRestClient;
import se.jg.mashup.integration.artist.dto.ArtistResponse;
import se.jg.mashup.integration.artist.dto.Relation;
import se.jg.mashup.integration.artist.dto.RelationsType;
import se.jg.mashup.integration.artist.dto.ReleaseGroup;
import se.jg.mashup.integration.coverart.CoverArtRestClient;
import se.jg.mashup.integration.coverart.dto.CoverArt;
import se.jg.mashup.integration.description.DescriptionRestClient;
import se.jg.mashup.integration.descriptionid.DescriptionIdLookupRestClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArtistService {

    private final ArtistRestClient artistRestClient;
    private final DescriptionIdLookupRestClient descriptionIdLookupRestClient;
    private final DescriptionRestClient descriptionRestClient;
    private final CoverArtRestClient coverArtRestClient;

    public ArtistService(ArtistRestClient artistRestClient, DescriptionIdLookupRestClient descriptionIdLookupRestClient, DescriptionRestClient descriptionRestClient, CoverArtRestClient coverArtRestClient) {
        this.artistRestClient = artistRestClient;
        this.descriptionIdLookupRestClient = descriptionIdLookupRestClient;
        this.descriptionRestClient = descriptionRestClient;
        this.coverArtRestClient = coverArtRestClient;
    }

    public Artist getArtist(String mbid) {
        ArtistResponse detailsForArtist = artistRestClient.getArtist(mbid);
        String description = getArtistDescription(detailsForArtist.getRelations());
        List<Album> albums = getAlbums(detailsForArtist.getReleasegroups());
        return Artist.builder()
                .mbid(detailsForArtist.getId())
                .description(description)
                .albums(albums)
                .build();
    }

    private String getArtistDescription(List<Relation> relationList) {
        if(relationList == null) {
            return null;
        }
        Map<RelationsType, Relation> wikiRelations = relationList.stream()
                .filter(RelationsType::isOfType)
                .collect(Collectors.toMap(r -> RelationsType.valueOf(r.getType()), Function.identity()));
        Relation wikiDataRelation = wikiRelations.get(RelationsType.wikidata);
        Relation wikipediaRelation = wikiRelations.get(RelationsType.wikipedia);
        if (wikipediaRelation != null) {
            return descriptionRestClient.getDescription(wikipediaRelation.getUrl().getResourceId());
        }
        Optional<String> descriptionId = Optional.empty();
        if(wikiDataRelation != null) {
            descriptionId = descriptionIdLookupRestClient.lookupDescriptionId(wikiDataRelation.getUrl().getResourceId());
        }
        return descriptionId.map(descriptionRestClient::getDescription).orElse(null);
    }

    private List<Album> getAlbums(List<ReleaseGroup> releaseGroups) {
        if(releaseGroups == null) {
            return new ArrayList<>();
        }
        String albumType = "Album";
        Map<String, String> coverArtImgByMbID = getCoverImageLinks(releaseGroups);
        return releaseGroups.stream()
                .filter(releaseGroup -> albumType.equalsIgnoreCase(releaseGroup.getPrimaryType()))
                .map(releaseGroup -> Album.builder()
                        .id(releaseGroup.getId())
                        .title(releaseGroup.getTitle())
                        .imageLink(coverArtImgByMbID.get(releaseGroup.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, String> getCoverImageLinks(List<ReleaseGroup> releaseGroups) {
        List<CompletableFuture<CoverArt>> imageLinks = releaseGroups.stream()
                .map(releaseGroup -> coverArtRestClient.getCoverArtLink(releaseGroup.getId()))
                .collect(Collectors.toList());

        return imageLinks.stream()
                .map(CompletableFuture::join)
                .filter(this::isValidCoverArt)
                .collect(Collectors.toMap(CoverArt::getCoverArtMbid, CoverArt::getImage));
    }

    private boolean isValidCoverArt(CoverArt coverArt) {
        return coverArt != null && coverArt.getCoverArtMbid() != null && coverArt.getImage() != null;
    }
}
