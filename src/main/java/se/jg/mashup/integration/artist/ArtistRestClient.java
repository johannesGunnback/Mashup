package se.jg.mashup.integration.artist;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import se.jg.mashup.config.URLConfigProperties;
import se.jg.mashup.integration.AbstractRestClient;
import se.jg.mashup.integration.EndpointBuilder;
import se.jg.mashup.integration.artist.dto.ArtistResponse;

import java.net.URISyntaxException;

import static se.jg.mashup.config.CacheConfiguration.CACHE_MANAGER;

@Service
public class ArtistRestClient extends AbstractRestClient {
    private static final String ARTIST_RESOURCE = "/artist/{mbid}";
    private static final String FORMAT = "fmt";
    private static final String INCLUDE = "inc";

    public ArtistRestClient(RestTemplate restTemplate, URLConfigProperties URLConfigProperties) throws URISyntaxException {
        super(restTemplate,  new EndpointBuilder(URLConfigProperties.getMusicbrainzUrl()));
    }

    @Cacheable(value = "longTerm", key = "'mbid_' + #mbid", cacheManager = CACHE_MANAGER)
    public ArtistResponse getArtist(String mbid) {
        acquireRateLimit(); // This is to be safe as the api will lock us out if we call it more than one time per second
        UriComponents uriComponents = endpointBuilder.getBuilder(ARTIST_RESOURCE)
                .queryParam(FORMAT, "json")
                .queryParam(INCLUDE,"url-rels+release-groups")
                .buildAndExpand(mbid);
        return restTemplate.getForObject(uriComponents.toUriString(), ArtistResponse.class);
    }
}
