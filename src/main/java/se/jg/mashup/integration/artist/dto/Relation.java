package se.jg.mashup.integration.artist.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Relation {
    private String type;
    private RelationUrl url;
}
