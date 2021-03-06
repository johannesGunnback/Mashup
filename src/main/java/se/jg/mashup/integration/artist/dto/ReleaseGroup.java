package se.jg.mashup.integration.artist.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReleaseGroup {

    private String id;
    private String title;
    @JsonAlias("primary-type")
    private String primaryType;
}
