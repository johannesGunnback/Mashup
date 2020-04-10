package se.jg.mashup.integration.artist.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseGroupTest {

    @Test
    public void shouldGetResourceId() {
        RelationUrl relationUrl = new RelationUrl();
        relationUrl.setResource("https://www.wikidata.org/wiki/Q11649");
        assertThat(relationUrl.getResourceId()).isEqualTo("Q11649");
    }

    @Test
    public void testWithNullResourceId() {
        RelationUrl relationUrl = new RelationUrl();
        assertThat(relationUrl.getResourceId()).isNull();
    }
}