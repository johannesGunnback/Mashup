package se.jg.mashup.integration.coverart.dto;

import lombok.Data;

@Data
public class CoverArt {

    private String image;
    private String coverArtMbid;

    public CoverArt() {}
    public CoverArt(String coverArtMbid) {
        this.coverArtMbid = coverArtMbid;
    }
}
