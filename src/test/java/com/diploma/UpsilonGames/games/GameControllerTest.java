package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CrossOrigin(origins = "*")
@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
//@SpringBootTest
class GameControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void getGameLong_shouldReturnGame() throws Exception{
        Game game = new Game("Far cry",1000,"Description");
        given(gameService.findGameByName(anyString())).willReturn(game);
        String result = mockMvc.perform(get("/games/Far cry/long"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(result,TestUtils.asJsonString(game));
        verify(gameService).findGameByName(game.getName());
    }
    @Test
    public void saveGame() throws Exception{
        Game game = new Game("Stellaris",1399,"Space strategy");
        given(gameService.save(any())).willReturn(game);
        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(game))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(gameService).save(any());
    }
}