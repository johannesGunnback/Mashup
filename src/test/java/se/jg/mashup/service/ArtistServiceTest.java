package se.jg.mashup.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import se.jg.mashup.dto.Album;
import se.jg.mashup.dto.Artist;

import java.util.Scanner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
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
    public void setUp() throws Exception {
        String musicbrainzData = readTestDataFile("/testdata/musicbrainz.json");

        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo("http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json&inc=url-rels+release-groups"))
                .andRespond(withSuccess(musicbrainzData, MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCallingGetUserDetails_thenClientMakesCorrectCall() {
        Artist artist = service.getArtist("5b11f4ce-a62d-471e-81fc-a69a8278c7da");

        assertThat(artist.getMbid()).isEqualTo("5b11f4ce-a62d-471e-81fc-a69a8278c7da");
    }

    private String readTestDataFile(String filePath) {
        Scanner scanner = new Scanner(this.getClass().getResourceAsStream(filePath));
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.nextLine());
        }
        return sb.toString();

    }
}