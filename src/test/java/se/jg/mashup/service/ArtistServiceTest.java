package se.jg.mashup.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.jg.mashup.dto.Album;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.integration.artist.ArtistRestClient;
import se.jg.mashup.integration.artist.dto.*;
import se.jg.mashup.integration.coverart.CoverArtRestClient;
import se.jg.mashup.integration.coverart.dto.CoverArt;
import se.jg.mashup.integration.description.DescriptionRestClient;
import se.jg.mashup.integration.descriptionid.DescriptionIdLookupRestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.*;

public class ArtistServiceTest {

    public static final String MBID_1 = "MBID_1";
    private static final String LINK_ID1 = "LINK_ID1";
    private static final String DESCRIPTION_ID1 = "DESCRIPTION_ID1";
    private static final String DESCRIPTION_1 = "DESCRIPTION_1";
    private static final String COVERART_ID1 = "COVERART_ID1";
    private static final String IMAGE_LINK1 = "IMAGE_LINK1";
    private static final String TITLE1 = "TITLE1";
    @Mock
    private ArtistRestClient artistRestClient;
    @Mock
    private DescriptionIdLookupRestClient descriptionIdLookupRestClient;
    @Mock
    private DescriptionRestClient descriptionRestClient;
    @Mock
    private CoverArtRestClient coverArtRestClient;

    private ArtistService artistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        artistService = new ArtistService(artistRestClient, descriptionIdLookupRestClient, descriptionRestClient, coverArtRestClient);
        when(artistRestClient.getArtist(MBID_1)).thenReturn(ArtistResponse.builder()
                .id(MBID_1)
                .releasegroups(Collections.singletonList(ReleaseGroup.builder()
                        .primaryType("album")
                        .title(TITLE1)
                        .id(COVERART_ID1)
                        .build()))
                .relations(Collections.singletonList(Relation.builder()
                        .type(RelationsType.wikidata.name())
                        .url(RelationUrl.builder()
                                .resource("/"+ LINK_ID1)
                                .build())
                        .build()))
                .build());
        when(descriptionIdLookupRestClient.lookupDescriptionId(LINK_ID1)).thenReturn(Optional.of(DESCRIPTION_ID1));
        when(descriptionRestClient.getDescription(DESCRIPTION_ID1)).thenReturn(DESCRIPTION_1);
        when(coverArtRestClient.getCoverArtLink(COVERART_ID1)).thenReturn(CompletableFuture.completedFuture(CoverArt.builder()
                .coverArtMbid(COVERART_ID1)
                .image(IMAGE_LINK1)
                .build()));
    }

    @Test
    public void verifyNormalFlow(){
        Artist artist = artistService.getArtist(MBID_1);
        assertThat(artist.getMbid()).isEqualTo(MBID_1);
        assertThat(artist.getDescription()).isEqualTo(DESCRIPTION_1);
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple(COVERART_ID1, TITLE1, IMAGE_LINK1));

    }

    @Test
    public void verifyWikipediaDirectLinkFlow(){
        when(artistRestClient.getArtist(MBID_1)).thenReturn(ArtistResponse.builder()
                .id(MBID_1)
                .releasegroups(Collections.singletonList(ReleaseGroup.builder()
                        .primaryType("album")
                        .title(TITLE1)
                        .id(COVERART_ID1)
                        .build()))
                .relations(Collections.singletonList(Relation.builder()
                        .type(RelationsType.wikipedia.name())
                        .url(RelationUrl.builder()
                                .resource("/"+ DESCRIPTION_ID1)
                                .build())
                        .build()))
                .build());

        Artist artist = artistService.getArtist(MBID_1);
        verifyNoInteractions(descriptionIdLookupRestClient);
        assertThat(artist.getMbid()).isEqualTo(MBID_1);
        assertThat(artist.getDescription()).isEqualTo(DESCRIPTION_1);
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple(COVERART_ID1, TITLE1, IMAGE_LINK1));

    }

    @Test
    public void noRelationsShouldBeOk() {
        when(artistRestClient.getArtist(MBID_1)).thenReturn(ArtistResponse.builder()
                .id(MBID_1)
                .releasegroups(Collections.singletonList(ReleaseGroup.builder()
                        .primaryType("album")
                        .title(TITLE1)
                        .id(COVERART_ID1)
                        .build()))
                .build());

        Artist artist = artistService.getArtist(MBID_1);

        verifyNoInteractions(descriptionIdLookupRestClient);
        verifyNoInteractions(descriptionRestClient);
        assertThat(artist.getMbid()).isEqualTo(MBID_1);
        assertThat(artist.getDescription()).isNull();
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple(COVERART_ID1, TITLE1, IMAGE_LINK1));
    }

    @Test
    public void emptyRelationsShouldBeOk() {
        when(artistRestClient.getArtist(MBID_1)).thenReturn(ArtistResponse.builder()
                .id(MBID_1)
                .relations(new ArrayList<>())
                .releasegroups(Collections.singletonList(ReleaseGroup.builder()
                        .primaryType("album")
                        .title(TITLE1)
                        .id(COVERART_ID1)
                        .build()))
                .build());

        Artist artist = artistService.getArtist(MBID_1);

        verifyNoInteractions(descriptionIdLookupRestClient);
        verifyNoInteractions(descriptionRestClient);
        assertThat(artist.getMbid()).isEqualTo(MBID_1);
        assertThat(artist.getDescription()).isNull();
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple(COVERART_ID1, TITLE1, IMAGE_LINK1));
    }

    @Test
    public void noReleaseGroupsShouldBeOK(){
        when(artistRestClient.getArtist(MBID_1)).thenReturn(ArtistResponse.builder()
                .id(MBID_1)
                .relations(Collections.singletonList(Relation.builder()
                        .type(RelationsType.wikipedia.name())
                        .url(RelationUrl.builder()
                                .resource("/"+ DESCRIPTION_ID1)
                                .build())
                        .build()))
                .build());

        Artist artist = artistService.getArtist(MBID_1);
        verifyNoInteractions(descriptionIdLookupRestClient);
        verifyNoInteractions(coverArtRestClient);
        assertThat(artist.getMbid()).isEqualTo(MBID_1);
        assertThat(artist.getDescription()).isEqualTo(DESCRIPTION_1);
    }

    @Test
    public void emptyReleaseGroupsShouldBeOK(){
        when(artistRestClient.getArtist(MBID_1)).thenReturn(ArtistResponse.builder()
                .id(MBID_1)
                .releasegroups(new ArrayList<>())
                .relations(Collections.singletonList(Relation.builder()
                        .type(RelationsType.wikipedia.name())
                        .url(RelationUrl.builder()
                                .resource("/"+ DESCRIPTION_ID1)
                                .build())
                        .build()))
                .build());

        Artist artist = artistService.getArtist(MBID_1);
        verifyNoInteractions(descriptionIdLookupRestClient);
        verifyNoInteractions(coverArtRestClient);
        assertThat(artist.getMbid()).isEqualTo(MBID_1);
        assertThat(artist.getDescription()).isEqualTo(DESCRIPTION_1);
    }
}
