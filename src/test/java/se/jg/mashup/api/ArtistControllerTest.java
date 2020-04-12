package se.jg.mashup.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.jg.mashup.dto.Artist;
import se.jg.mashup.exception.ErrorCode;
import se.jg.mashup.exception.ResourceNotFound;
import se.jg.mashup.service.ArtistService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArtistService artistService;

    @Test
    public void shouldCallEndpointOk() throws Exception {
        Artist artist = Artist.builder().description("description").build();
        when(artistService.getArtist(anyString())).thenReturn(artist);

        mvc.perform(get("/api/artist/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("description")));
    }

    @Test
    public void shouldGiverNotFound() throws Exception {
        when(artistService.getArtist(anyString())).thenThrow(new ResourceNotFound("Not found", ErrorCode.ARTIST_NOT_FOUND));

        mvc.perform(get("/api/artist/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}