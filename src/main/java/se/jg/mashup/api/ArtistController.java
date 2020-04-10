package se.jg.mashup.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jg.mashup.dto.Artist;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    @GetMapping( "{mbid}")
    public Artist getArtist(@PathVariable String mbid) {
        return Artist.builder().mbid(mbid).build();
    }
}
