package se.jg.mashup.integration.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import se.jg.mashup.config.URLConfigProperties;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

class ArtistRestClientTest {

    private ArtistRestClient artistRestClient;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private URLConfigProperties uriConfig;


    @BeforeEach
    public void setUp() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);
        when(uriConfig.getMusicbrainzUrl()).thenReturn("url.se");
        artistRestClient = new ArtistRestClient(restTemplate, uriConfig);
    }

    @Test
    public void testRateLimit() {
        long startTime = ZonedDateTime.now().getSecond();
        artistRestClient.getArtist("id1");
        artistRestClient.getArtist("id2");
        artistRestClient.getArtist("id3");
        long elapsedTimeSeconds = ZonedDateTime.now().getSecond() - startTime;
        assertThat(elapsedTimeSeconds).isGreaterThanOrEqualTo(2);
    }


}