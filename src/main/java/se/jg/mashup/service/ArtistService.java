package se.jg.mashup.service;

import org.springframework.stereotype.Service;
import se.jg.mashup.dto.Album;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.integration.artist.ArtistRestClient;
import se.jg.mashup.integration.artist.dto.ArtistResponse;
import se.jg.mashup.integration.artist.dto.Relation;
import se.jg.mashup.integration.artist.dto.RelationsType;
import se.jg.mashup.integration.artist.dto.ReleaseGroup;
import se.jg.mashup.integration.coverart.CoverArtRestClient;
import se.jg.mashup.integration.description.DescriptionRestClient;
import se.jg.mashup.integration.descriptionid.DescriptionIdLookupRestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<RelationsType, Relation> wikiRelations = relationList.stream()
                .filter(RelationsType::isOfType)
                .collect(Collectors.toMap(r -> RelationsType.valueOf(r.getType()), Function.identity()));
        Relation wikiDataRelation = wikiRelations.get(RelationsType.wikidata);
        Relation wikipediaRelation = wikiRelations.get(RelationsType.wikipedia);
        if (wikipediaRelation != null) {
            //TODO call wikipedia
            return null;
        }
        Optional<String> descriptionId = descriptionIdLookupRestClient.lookupDescriptionId(wikiDataRelation.getUrl().getResourceId());
        if (descriptionId.isPresent()) {
            return descriptionRestClient.getDescription(descriptionId.get());
        }
        return null;
    }

    private List<Album> getAlbums(List<ReleaseGroup> releaseGroups) {
        String albumType = "Album";
        return releaseGroups.stream()
                .filter(releaseGroup -> albumType.equalsIgnoreCase(releaseGroup.getPrimaryType()))
                .map(releaseGroup -> Album.builder()
                        .id(releaseGroup.getId())
                        .title(releaseGroup.getTitle())
                        .imageLink(coverArtRestClient.getCoverArtLink(releaseGroup.getId())) //TODO see if we can call this async
                        .build())
                .collect(Collectors.toList());
    }
}
