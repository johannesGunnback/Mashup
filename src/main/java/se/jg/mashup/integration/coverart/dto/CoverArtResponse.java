package se.jg.mashup.integration.coverart.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CoverArtResponse {
    private List<CoverArt> images;

    public CoverArtResponse() {
        images = new ArrayList<>();
    }
}
