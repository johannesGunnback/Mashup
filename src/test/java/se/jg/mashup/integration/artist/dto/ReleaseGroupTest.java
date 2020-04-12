package se.jg.mashup.integration.artist.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseGroupTest {

    @Test
    public void shouldGetResourceIdForWikidata() {
        RelationUrl relationUrl = RelationUrl.builder()
                .resource("https://www.wikidata.org/wiki/Q11649")
                .build();
        assertThat(relationUrl.getResourceId()).isEqualTo("Q11649");
    }

    @Test
    public void shouldGetResourceIdForWikipedia() {
        RelationUrl relationUrl = RelationUrl.builder()
                .resource("https://en.wikipedia.org/wiki/The_Temptations")
                .build();
        assertThat(relationUrl.getResourceId()).isEqualTo("The_Temptations");
    }

    @Test
    public void testWithNullResourceId() {
        RelationUrl relationUrl = RelationUrl.builder().build();
        assertThat(relationUrl.getResourceId()).isNull();
    }
}