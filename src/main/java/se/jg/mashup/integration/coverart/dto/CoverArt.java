package se.jg.mashup.integration.coverart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoverArt {

    private String image;
    private String coverArtMbid;
}
