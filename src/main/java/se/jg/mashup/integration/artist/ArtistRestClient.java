package se.jg.mashup.integration.artist;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.jg.mashup.integration.artist.dto.ArtistResponse;

@Service
public class ArtistRestClient {

    private final RestTemplate restTemplate;

    public ArtistRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ArtistResponse getArtist(String mbid) {
        return restTemplate.getForObject("http://musicbrainz.org/ws/2/artist/"+mbid+"?fmt=json&inc=url-rels+release-groups", ArtistResponse.class);
    }
}
