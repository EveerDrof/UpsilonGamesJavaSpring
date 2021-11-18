package com.diploma.UpsilonGames.pictures;

import com.diploma.UpsilonGames.BlobHelper;
import com.diploma.UpsilonGames.TestUtils;
import com.diploma.UpsilonGames.games.Game;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PictureServiceTest {
    private static PictureService pictureService;
    @Mock
    private PictureRepository pictureRepository;
    private static Game game;
    private static Picture picture;
    @BeforeEach
    public void setUp() throws Exception {
        SessionFactory mockedSessionFactory =  Mockito.mock(SessionFactory.class);
        Session mockedSession = Mockito.mock(Session.class);
        LobHelper lobHelper = Mockito.mock(LobHelper.class);
        Blob blob = Mockito.mock(Blob.class);
        Mockito.when(mockedSessionFactory.getCurrentSession()).thenReturn(mockedSession);
        Mockito.when(mockedSession.getLobHelper()).thenReturn(lobHelper);
        Mockito.when(lobHelper.createBlob(any(InputStream.class),any(Long.class))).thenReturn(blob);
        TestUtils.setSessionFactory(mockedSessionFactory);
        pictureService = new PictureService(pictureRepository);
        game = new Game("PictureServiceTest game",1,"asdfsadfsad");
        picture = TestUtils.getUniqueTestPicture(game);
        given(pictureRepository.save(picture)).willReturn(picture);
    }
    @Test
    public void save() throws Exception{
        Picture result = pictureService.save(picture);
        verify(pictureRepository).save(picture);
        Assertions.assertEquals(result, picture);
    }
    @Test
    public void findByExistingId_shouldReturnPicture() throws Exception{
        Picture saveResult = pictureService.save(picture);
        Assertions.assertEquals(saveResult, picture);
        doReturn(Optional.of(saveResult)).when(pictureRepository).findById(saveResult.getId());
        Picture findResult = pictureService.findById(saveResult.getId());
        Assertions.assertEquals(saveResult, findResult);
        verify(pictureRepository).findById(saveResult.getId());
    }
    @Test
    public void  findAll_shouldReturnArray(){
        Picture saveResult = pictureService.save(picture);
        Assertions.assertEquals(saveResult, picture);
        ArrayList list = new ArrayList();
        list.add(picture);
        doReturn(list).when(pictureRepository).findAll();
        ArrayList<Picture> findResult = pictureService.findAll();
        Assertions.assertEquals(saveResult, findResult.get(0));
        verify(pictureRepository).findAll();
    }
}
