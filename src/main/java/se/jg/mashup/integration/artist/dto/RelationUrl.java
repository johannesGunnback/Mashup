package se.jg.mashup.integration.artist.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

@Data
@Builder
public class RelationUrl {
    private String resource;
    private String id;

    // TODO As for now we assume that all resource strings for wikidata and wikipedia has the same format, need to confirm that.
    public String getResourceId() {
        if(StringUtils.isEmpty(resource)) {
            return null;
        }
        String[] split = resource.split("/");
        return Stream.of(split).reduce((first, second) -> second)
                .orElse(null);
    }
}
