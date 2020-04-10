package se.jg.mashup.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Album {

    private final String id;
    private final String title;
    private final String imageLink;

}
