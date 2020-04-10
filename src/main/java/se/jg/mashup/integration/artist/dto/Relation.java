package se.jg.mashup.integration.artist.dto;

import lombok.Data;

@Data
public class Relation {
    private String type;
    private RelationUrl url;
}
