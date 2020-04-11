package se.jg.mashup.integration.artist.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseGroupTest {

    @Test
    public void shouldGetResourceId() {
        RelationUrl relationUrl = RelationUrl.builder()
                .resource("https://www.wikidata.org/wiki/Q11649")
                .build();
        assertThat(relationUrl.getResourceId()).isEqualTo("Q11649");
    }

    @Test
    public void testWithNullResourceId() {
        RelationUrl relationUrl = RelationUrl.builder().build();
        assertThat(relationUrl.getResourceId()).isNull();
    }
}