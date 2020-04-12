package se.jg.mashup.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.exception.ResourceNotFound;
import se.jg.mashup.service.ArtistService;

@Slf4j
@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping( "{mbid}")
    public Artist getArtist(@PathVariable String mbid) {
        try {
            return artistService.getArtist(mbid);
        } catch(ResourceNotFound e) {
            log.error("Error in artist get", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getErrorCode().getMsg());
        }
    }
}
