package se.jg.mashup.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.service.ArtistService;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping( "{mbid}")
    public Artist getArtist(@PathVariable String mbid) {
        return artistService.getArtist(mbid);
    }
}
