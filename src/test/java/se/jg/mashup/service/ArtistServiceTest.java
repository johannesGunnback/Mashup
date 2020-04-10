package se.jg.mashup.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import se.jg.mashup.TestDataUtils;
import se.jg.mashup.dto.Album;
import se.jg.mashup.dto.Artist;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class ArtistServiceTest {

    public static final String MUSICABRAINZ_URL_MATCH = "http://fake/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json&inc=url-rels+release-groups";
    public static final String WIKIDATA_URL_MATCH = "http://fake?action=wbgetentities&ids=Q11649&format=json&props=sitelinks";
    public static final String WIKIPEDIA_URL_MATCH = "http://fake?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=Nirvana%20(band)";
    public static final String COVERART_URL_MATCH = "http://fake/release-group/1b022e01-4da6-387b-8658-8678046e4cef";

    @Autowired
    private ArtistService service;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        String musicbrainzData = TestDataUtils.readTestDataFile("/testdata/musicbrainz.json");
        String wikidata = TestDataUtils.readTestDataFile("/testdata/wikidata.json");
        String wikipedia = TestDataUtils.readTestDataFile("/testdata/wikipedia.json");
        String coverart = TestDataUtils.readTestDataFile("/testdata/coverart.json");

        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo(MUSICABRAINZ_URL_MATCH)).andRespond(withSuccess(musicbrainzData, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIDATA_URL_MATCH)).andRespond(withSuccess(wikidata, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_URL_MATCH)).andRespond(withSuccess(wikipedia, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(COVERART_URL_MATCH)).andRespond(withSuccess(coverart, MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldFetchArtist() {
        Artist artist = service.getArtist("5b11f4ce-a62d-471e-81fc-a69a8278c7da");

        assertThat(artist.getMbid()).isEqualTo("5b11f4ce-a62d-471e-81fc-a69a8278c7da");
        assertThat(artist.getDescription()).isNotEmpty();
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple("1b022e01-4da6-387b-8658-8678046e4cef", "Nevermind", "http://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cdc2d/3012495605.jpg"));

    }

}