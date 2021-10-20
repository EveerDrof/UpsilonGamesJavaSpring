package com.diploma.UpsilonGames.games;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GameServiceUnitTest {

    private GameService gameService;

    @Mock
    private GameRepository gameRepository;
    @BeforeEach
    public void setUp() throws Exception{
        gameService = new GameService(gameRepository);
    }
    @Test
    public void getGameLong_shouldReturnGame()throws Exception{
        String gameName = "Far cry";
        given(gameRepository.findByName(anyString())).willReturn(new Game(gameName,2000));
        Game game = gameService.findGameByName(gameName);
        assertEquals(game.getName(),gameName);
        verify(gameRepository).findByName(anyString());
    }
    @Test
    public void saveGame()throws Exception{
        Game game = new Game("Far cry",200);
        given(gameRepository.save(game)).willReturn(game);
        Game returnedGame = gameService.save(game);
        assertEquals(returnedGame.getName(),game.getName());
        assertEquals(returnedGame.getId(),game.getId());
        verify(gameRepository).save(game);
    }
}
