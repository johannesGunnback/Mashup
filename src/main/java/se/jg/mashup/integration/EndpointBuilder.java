package se.jg.mashup.integration;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class EndpointBuilder {
    private static final String DEFAULT_SCHEME = "http";

    private final URI baseUri;

    public EndpointBuilder(String baseUrl) throws URISyntaxException {
        this.baseUri = new URI(baseUrl);
    }

    public UriComponentsBuilder getBuilder(String path) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.scheme(getScheme());
        builder.host(baseUri.getHost());
        builder.path(getPath(path));
        return builder;
    }

    private String getPath(String path) {
        if(StringUtils.isEmpty(baseUri.getPath())) {
            return path;
        }
        return baseUri.getPath() + path;
    }

    private String getScheme() {
        if(StringUtils.isEmpty(baseUri.getScheme())) {
            return DEFAULT_SCHEME;
        }
        return baseUri.getScheme();
    }
}
