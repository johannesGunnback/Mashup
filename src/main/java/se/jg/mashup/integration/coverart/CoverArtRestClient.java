package se.jg.mashup.integration.coverart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import se.jg.mashup.config.URLConfigProperties;
import se.jg.mashup.integration.AbstractRestClient;
import se.jg.mashup.integration.EndpointBuilder;
import se.jg.mashup.integration.coverart.dto.CoverArt;
import se.jg.mashup.integration.coverart.dto.CoverArtResponse;

import java.net.URISyntaxException;

import static se.jg.mashup.config.CacheConfiguration.CACHE_MANAGER;

@Slf4j
@Service
public class CoverArtRestClient extends AbstractRestClient {

    private static final String RELEASE_GROUP_RESOURCE = "/release-group/{mbid}";

    public CoverArtRestClient(RestTemplate restTemplate, URLConfigProperties URLConfigProperties) throws URISyntaxException {
        super(restTemplate, new EndpointBuilder(URLConfigProperties.getCoverartarchiveUrl()));
    }

    @Cacheable(value = "longTerm", key = "'coverArtMbid_' + #coverArtMbid", cacheManager = CACHE_MANAGER)
    public String getCoverArtLink(String coverArtMbid) {
        UriComponents uriComponents = endpointBuilder.getBuilder(RELEASE_GROUP_RESOURCE).buildAndExpand(coverArtMbid);
        try {
            CoverArtResponse coverArtResponse = restTemplate.getForObject(uriComponents.toUriString(), CoverArtResponse.class);
            if(coverArtResponse == null || coverArtResponse.getImages().isEmpty()){
                log.warn("could not find cover art for id: {}", coverArtMbid);
                return null;
            }
            return coverArtResponse.getImages().stream().findFirst().orElse(new CoverArt()).getImage();
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("could not find cover art for id: {}", coverArtMbid);
                return null;
            }
            log.error(e.getMessage());
            throw e;
        }

    }
}
