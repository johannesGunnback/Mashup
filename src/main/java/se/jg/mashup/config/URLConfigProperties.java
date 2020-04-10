package se.jg.mashup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "restapi")
public class URLConfigProperties {
   private String musicbrainzUrl;
   private String wikidataUrl;
   private String wikipediaUrl;
   private String coverartarchiveUrl;
}
