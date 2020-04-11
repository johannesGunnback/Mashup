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
public class ArtistServiceIntegrationTest {

    public static final String MUSICABRAINZ_URL_MATCH = "http://fake/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json&inc=url-rels+release-groups";
    public static final String MUSICABRAINZ_URL_MATCH_2 = "http://fake/artist/d8df96ae-8fcf-4997-b3e6-e5d1aaf0f69e?fmt=json&inc=url-rels+release-groups";
    public static final String WIKIDATA_URL_MATCH = "http://fake?action=wbgetentities&ids=Q11649&format=json&props=sitelinks";
    public static final String WIKIPEDIA_URL_MATCH = "http://fake?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=Nirvana%20(band)";
    public static final String WIKIPEDIA_URL_MATCH_2 = "http://fake?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=The_Temptations";
    public static final String COVERART_URL_MATCH = "http://fake/release-group/1b022e01-4da6-387b-8658-8678046e4cef";
    public static final String COVERART_URL_MATCH_2 = "http://fake/release-group/709f599f-f9c6-3d39-b97c-ed058f95c732";

    @Autowired
    private ArtistService service;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFetchArtist() {
        setUpTestDataForStandardFlow();

        Artist artist = service.getArtist("5b11f4ce-a62d-471e-81fc-a69a8278c7da");

        assertThat(artist.getMbid()).isEqualTo("5b11f4ce-a62d-471e-81fc-a69a8278c7da");
        assertThat(artist.getDescription()).isNotEmpty();
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple("1b022e01-4da6-387b-8658-8678046e4cef", "Nevermind", "http://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cdc2d/3012495605.jpg"));

    }

    @Test
    public void shouldBypassWikidata() {
        setUpTestDataForWikipediaDirectFlow();

        Artist artist = service.getArtist("d8df96ae-8fcf-4997-b3e6-e5d1aaf0f69e");

        assertThat(artist.getMbid()).isEqualTo("d8df96ae-8fcf-4997-b3e6-e5d1aaf0f69e");
        assertThat(artist.getDescription()).isNotEmpty();
        Assertions.assertThat(artist.getAlbums())
                .extracting(Album::getId, Album::getTitle, Album::getImageLink)
                .containsOnly(tuple("709f599f-f9c6-3d39-b97c-ed058f95c732", "Meet The Temptations", "http://coverartarchive.org/release/8eed59e6-be94-4544-9309-dc4686a800a1/12411927261.jpg"));
    }

    private void setUpTestDataForWikipediaDirectFlow() {
        String musicbrainzData = TestDataUtils.readTestDataFile("/testdata/musicbrainz_wikipedia_url.json");
        String wikipedia = TestDataUtils.readTestDataFile("/testdata/wikipedia_direct_link.json");
        String coverart = TestDataUtils.readTestDataFile("/testdata/coverart_wikipedia_direct_link.json");

        mockServer.expect(requestTo(MUSICABRAINZ_URL_MATCH_2)).andRespond(withSuccess(musicbrainzData, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_URL_MATCH_2)).andRespond(withSuccess(wikipedia, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(COVERART_URL_MATCH_2)).andRespond(withSuccess(coverart, MediaType.APPLICATION_JSON));
    }

    private void setUpTestDataForStandardFlow() {
        String musicbrainzData = TestDataUtils.readTestDataFile("/testdata/musicbrainz.json");
        String wikidata = TestDataUtils.readTestDataFile("/testdata/wikidata.json");
        String wikipedia = TestDataUtils.readTestDataFile("/testdata/wikipedia.json");
        String coverart = TestDataUtils.readTestDataFile("/testdata/coverart.json");

        mockServer.expect(requestTo(MUSICABRAINZ_URL_MATCH)).andRespond(withSuccess(musicbrainzData, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIDATA_URL_MATCH)).andRespond(withSuccess(wikidata, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_URL_MATCH)).andRespond(withSuccess(wikipedia, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(COVERART_URL_MATCH)).andRespond(withSuccess(coverart, MediaType.APPLICATION_JSON));
    }

}