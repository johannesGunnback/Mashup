package se.jg.mashup.integration.coverart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import se.jg.mashup.config.URLConfigProperties;
import se.jg.mashup.integration.AbstractRestClient;
import se.jg.mashup.integration.EndpointBuilder;
import se.jg.mashup.integration.coverart.dto.CoverArt;
import se.jg.mashup.integration.coverart.dto.CoverArtResponse;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import static se.jg.mashup.config.CacheConfiguration.CACHE_MANAGER;

@Slf4j
@Service
public class CoverArtRestClient extends AbstractRestClient {

    private static final String RELEASE_GROUP_RESOURCE = "/release-group/{mbid}";

    public CoverArtRestClient(RestTemplate restTemplate, URLConfigProperties URLConfigProperties) throws URISyntaxException {
        super(restTemplate, new EndpointBuilder(URLConfigProperties.getCoverartarchiveUrl()));
    }

    @Async
    @Cacheable(value = "longTerm", key = "'coverArtMbid_' + #coverArtMbid", cacheManager = CACHE_MANAGER)
    public CompletableFuture<CoverArt> getCoverArtLink(String coverArtMbid) {
        UriComponents uriComponents = endpointBuilder.getBuilder(RELEASE_GROUP_RESOURCE).buildAndExpand(coverArtMbid);
        try {
            CoverArtResponse coverArtResponse = restTemplate.getForObject(uriComponents.toUriString(), CoverArtResponse.class);
            return parseResponse(coverArtMbid, coverArtResponse);
        } catch (Exception e) {
            log.warn("could not find cover art for id: {}", coverArtMbid);
            return CompletableFuture.completedFuture(CoverArt.builder().build());
        }
    }

    private CompletableFuture<CoverArt> parseResponse(String coverArtMbid, CoverArtResponse coverArtResponse) {
        if (coverArtResponse == null || coverArtResponse.getImages().isEmpty()) {
            log.warn("could not find cover art for id: {}", coverArtMbid);
            return CompletableFuture.completedFuture(CoverArt.builder().build());
        }
        CoverArt coverArt = coverArtResponse.getImages().stream().findFirst().orElse(CoverArt.builder().build());
        coverArt.setCoverArtMbid(coverArtMbid);
        return CompletableFuture.completedFuture(coverArt);
    }
}
