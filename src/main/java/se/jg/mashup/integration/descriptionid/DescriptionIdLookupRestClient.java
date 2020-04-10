package se.jg.mashup.integration.descriptionid;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import se.jg.mashup.config.URLConfigProperties;
import se.jg.mashup.integration.AbstractRestClient;
import se.jg.mashup.integration.EndpointBuilder;
import se.jg.mashup.integration.descriptionid.dto.LookupEntity;
import se.jg.mashup.integration.descriptionid.dto.LookupResponse;

import java.net.URISyntaxException;
import java.util.Optional;

import static se.jg.mashup.config.CacheConfiguration.CACHE_MANAGER;

@Service
public class DescriptionIdLookupRestClient extends AbstractRestClient {

    public DescriptionIdLookupRestClient(RestTemplate restTemplate, URLConfigProperties URLConfigProperties) throws URISyntaxException {
        super(restTemplate, new EndpointBuilder(URLConfigProperties.getWikidataUrl()));
    }

    @Cacheable(value = "longTerm", key = "'linkId_' + #linkId", cacheManager = CACHE_MANAGER)
    public Optional<String> lookupDescriptionId(String linkId) {
        acquireRateLimit(); // Did not find any notice of lookouts for wikidata but will use the same constraints to be safe.
        UriComponents uriComponents = endpointBuilder.getBuilder("")
                .queryParam("action", "wbgetentities")
                .queryParam("ids", linkId)
                .queryParam("format", "json")
                .queryParam("props", "sitelinks")
                .build();
        LookupResponse lookupResponse = restTemplate.getForObject(uriComponents.toUriString(), LookupResponse.class);
        LookupEntity lookupEntity = lookupResponse.getEntities().get(linkId);
        if (lookupEntity == null || lookupEntity.getSitelinks() == null || lookupEntity.getSitelinks().getEnwiki() == null) {
            return Optional.empty();
        }
        return Optional.of(lookupEntity.getSitelinks().getEnwiki().getTitle());
    }
}
