package se.jg.mashup.service;

import org.springframework.stereotype.Service;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.integration.artist.ArtistRestClient;
import se.jg.mashup.integration.artist.dto.ArtistResponse;

@Service
public class ArtistService {

    private final ArtistRestClient artistRestClient;

    public ArtistService(ArtistRestClient artistRestClient) {
        this.artistRestClient = artistRestClient;
    }

    public Artist getArtist(String mbid) {
        ArtistResponse detailsForArtist = artistRestClient.getArtist(mbid);
        return Artist.builder()
                .mbid(detailsForArtist.getId())
                .build();
    }
}
