package se.jg.mashup.integration.description;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import se.jg.mashup.config.URLConfigProperties;
import se.jg.mashup.integration.AbstractRestClient;
import se.jg.mashup.integration.EndpointBuilder;
import se.jg.mashup.integration.description.dto.DescriptionResponse;
import se.jg.mashup.integration.description.dto.Page;

import java.net.URISyntaxException;
import java.util.Optional;

import static se.jg.mashup.config.CacheConfiguration.CACHE_MANAGER;

@Service
public class DescriptionRestClient extends AbstractRestClient {

    public DescriptionRestClient(RestTemplate restTemplate, URLConfigProperties URLConfigProperties) throws URISyntaxException {
        super(restTemplate, new EndpointBuilder(URLConfigProperties.getWikipediaUrl()));
    }

    @Cacheable(value = "longTerm", key = "'descriptionId_' + #descriptionId", cacheManager = CACHE_MANAGER)
    public String getDescription(String descriptionId) {
        UriComponents uriComponents = endpointBuilder.getBuilder("")
                .queryParam("action", "query")
                .queryParam("format","json")
                .queryParam("prop","extracts")
                .queryParam("exintro",true)
                .queryParam("redirects",true) //TODO not sure if this is needed
                .queryParam("titles",descriptionId)
                .build();
        DescriptionResponse response = restTemplate.getForObject(uriComponents.toUriString(), DescriptionResponse.class);
        if (response == null || response.getQuery() == null) {
            return null;
        }
        Optional<Page> firstPage = response.getQuery().getPages().values().stream().findFirst(); // We are only interested in the first description we find.
        if(!firstPage.isPresent()) {
            return null;
        }
        return firstPage.get().getExtract();

    }
}
