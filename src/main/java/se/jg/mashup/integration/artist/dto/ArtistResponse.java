package se.jg.mashup.integration.artist.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class ArtistResponse {
    private String id;
    private List<Relation> relations;
    @JsonAlias("release-groups")
    private List<ReleaseGroup> releasegroups;
}
