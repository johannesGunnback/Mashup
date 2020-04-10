package se.jg.mashup.integration;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class EndpointBuilderTest {

    @Test
    public void shouldParseHttpUrl() throws URISyntaxException {
        EndpointBuilder endpointBuilder = new EndpointBuilder("http://musicbrainz.org/ws/2");
        UriComponentsBuilder uriBuilder = endpointBuilder.getBuilder("/path");
        assertThat(uriBuilder.toUriString()).isEqualTo("http://musicbrainz.org/ws/2/path");
    }

    @Test
    public void shouldParseHttpsUrl() throws URISyntaxException {
        EndpointBuilder endpointBuilder = new EndpointBuilder("https://www.wikidata.org/w/api.php");
        UriComponentsBuilder uriBuilder = endpointBuilder.getBuilder("/path");
        assertThat(uriBuilder.toUriString()).isEqualTo("https://www.wikidata.org/w/api.php/path");
    }

    @Test
    public void shouldParseUrlWithoutScheme() throws URISyntaxException {
        EndpointBuilder endpointBuilder = new EndpointBuilder("musicbrainz.org/ws/2");
        UriComponentsBuilder uriBuilder = endpointBuilder.getBuilder("/path");
        assertThat(uriBuilder.toUriString()).isEqualTo("http:/musicbrainz.org/ws/2/path");
    }

}