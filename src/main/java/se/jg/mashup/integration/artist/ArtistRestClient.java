package se.jg.mashup.integration.artist;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.jg.mashup.integration.artist.dto.ArtistResponse;

import static se.jg.mashup.config.CacheConfiguration.CACHE_MANAGER;

@Service
public class ArtistRestClient {

    private final RestTemplate restTemplate;

    public ArtistRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "longTerm", key = "'mbid_' + #mbid", cacheManager = CACHE_MANAGER)
    public ArtistResponse getArtist(String mbid) {
        return restTemplate.getForObject("http://musicbrainz.org/ws/2/artist/"+mbid+"?fmt=json&inc=url-rels+release-groups", ArtistResponse.class);
    }
}
