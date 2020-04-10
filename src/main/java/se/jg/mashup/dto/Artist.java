package se.jg.mashup.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Artist {

    private final String mbid;
    private final String description;
    private final List<Album> albums;
}
