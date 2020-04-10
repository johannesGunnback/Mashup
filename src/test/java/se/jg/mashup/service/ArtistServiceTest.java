package se.jg.mashup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import se.jg.mashup.TestDataUtils;
import se.jg.mashup.dto.Artist;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class ArtistServiceTest {

    @Autowired
    private ArtistService service;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        String musicbrainzData = TestDataUtils.readTestDataFile("/testdata/musicbrainz.json");

        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo("http://fake/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json&inc=url-rels+release-groups"))
                .andRespond(withSuccess(musicbrainzData, MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldFetchArtist() {
        Artist artist = service.getArtist("5b11f4ce-a62d-471e-81fc-a69a8278c7da");

        assertThat(artist.getMbid()).isEqualTo("5b11f4ce-a62d-471e-81fc-a69a8278c7da");
    }

}