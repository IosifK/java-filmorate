package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
    }

    @Test
    public void testGetAllFilms() throws Exception {
        when(filmService.getAllFilms()).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Film"))
                .andExpect(jsonPath("$[0].description").value("Test description"))
                .andExpect(jsonPath("$[0].releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$[0].duration").value(120));
    }

    @Test
    public void testGetFilmById() throws Exception {
        when(filmService.getFilmById(1)).thenReturn(film);

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Film"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$.duration").value(120));
    }

    @Test
    public void testAddFilm() throws Exception {
        when(filmService.addFilm(any(Film.class))).thenReturn(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Film\", \"description\":\"Test description\", \"releaseDate\":\"2000-01-01\", \"duration\":120}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Film"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$.duration").value(120));
    }

    @Test
    public void testUpdateFilm() throws Exception {
        film.setName("Updated Film");
        film.setDescription("Updated description");

        when(filmService.updateFilm(any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"name\":\"Updated Film\", \"description\":\"Updated description\", \"releaseDate\":\"2000-01-01\", \"duration\":120}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Film"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$.duration").value(120));
    }

    @Test
    public void testAddLike() throws Exception {

        doNothing().when(filmService).addLike(1, 2);

        when(filmService.getFilmById(1)).thenReturn(film);

        mockMvc.perform(put("/films/1/like/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Film"));
    }


    @Test
    public void testRemoveLike() throws Exception {
        doNothing().when(filmService).removeLike(1, 2);

        when(filmService.getFilmById(1)).thenReturn(film);

        mockMvc.perform(delete("/films/1/like/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Film"));
    }


    @Test
    public void testGetMostPopularFilms() throws Exception {
        when(filmService.getMostPopularFilms(10)).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films/popular?count=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Film"))
                .andExpect(jsonPath("$[0].description").value("Test description"))
                .andExpect(jsonPath("$[0].releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$[0].duration").value(120));
    }
}

