package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.marks.MarkRepository;
import com.diploma.UpsilonGames.marks.MarkService;
import org.junit.jupiter.api.Assertions;
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
public class GameServiceTest {

    private GameService gameService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private MarkRepository markRepository;
    @BeforeEach
    public void setUp() throws Exception{
        gameService = new GameService(gameRepository,markRepository);
    }
    @Test
    public void getGameLong_shouldReturnGame()throws Exception{
        String gameName = "Far cry";
        given(gameRepository.findByName(anyString())).willReturn(new Game(gameName,2000));
        Game game = gameService.findByName(gameName);
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
    @Test
    void existsByIdOnNonExisting_shouldReturnFalse(){
        given(gameRepository.existsById(9999L)).willReturn(false);
        Assertions.assertFalse(gameService.existsById(9999L));
    }
    @Test
    void existsByIdOnExisting_shouldReturnTrue(){
        given(gameRepository.existsById(9999L)).willReturn(true);
        Assertions.assertTrue(gameService.existsById(9999L));
    }
    @Test
    void findById(){
        Game game = new Game(100,"aaa",2000,"adasda");
        given(gameRepository.getById(game.getId())).willReturn(game);
        Game result = gameService.findById(game.getId());
        Assertions.assertTrue(game.equals(result));
    }
}
