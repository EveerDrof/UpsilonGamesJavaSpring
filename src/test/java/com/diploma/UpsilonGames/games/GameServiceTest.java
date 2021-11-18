package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.TestUtils;
import com.diploma.UpsilonGames.marks.MarkRepository;
import com.diploma.UpsilonGames.marks.MarkService;
import com.diploma.UpsilonGames.pictures.Picture;
import com.diploma.UpsilonGames.pictures.PictureRepository;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameServiceTest {

    private GameService gameService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private MarkRepository markRepository;
    @Mock
    private PictureRepository pictureRepository;
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
    @Test
    void getShortcut() throws Exception{
        SessionFactory mockedSessionFactory =  Mockito.mock(SessionFactory.class);
        Session mockedSession = Mockito.mock(Session.class);
        LobHelper lobHelper = Mockito.mock(LobHelper.class);
        Blob blob = Mockito.mock(Blob.class);
        Mockito.when(mockedSessionFactory.getCurrentSession()).thenReturn(mockedSession);
        Mockito.when(mockedSession.getLobHelper()).thenReturn(lobHelper);
        Mockito.when(lobHelper.createBlob(any(InputStream.class),any(Long.class))).thenReturn(blob);
        TestUtils.setSessionFactory(mockedSessionFactory);
        Game game = new Game("getShortcutGame",2000,"adasda");
        Picture picture = TestUtils.getUniqueTestPicture(game);
        game.setShortcut(picture);
        given(gameRepository.findByName(game.getName())).willReturn(game);
        given(pictureRepository.getById(game.getShortcut().getId())).willReturn(picture);
        gameService.save(game);
        gameService.getShortcutByGameName(game.getName());
        verify(gameRepository).save(game);
    }
    @Test
    public void findAll(){
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game("asdfasfd",222,"asdffds"));
        games.add(new Game("2asdfasfd",333,"asdffds"));
        given(gameRepository.findAll()).willReturn(games);
        ArrayList<Game> result = gameService.findAll();
        for(int i=0;i<games.size();i++){
            Assertions.assertEquals(games.get(i),result.get(i));
        }
        verify(gameRepository).findAll();
    }
}
