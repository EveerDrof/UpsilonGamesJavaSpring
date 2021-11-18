package com.diploma.UpsilonGames.pictures;

import com.diploma.UpsilonGames.BlobHelper;
import com.diploma.UpsilonGames.TestUtils;
import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.sql.Blob;
import java.util.Random;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PictureRepositoryTest {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private SessionFactory sessionFactory;
    private static BlobHelper blobHelper;
    private static boolean isInitialized = false;
    private static Instrumentation instrumentation;
    private static Game game;
    private static Picture startPicture;
    @BeforeEach
    private void setUp() throws Exception {
        if (!isInitialized) {
            TestUtils.setSessionFactory(sessionFactory);
            game = gameRepository.save(new Game(
                    "Picture repository game test setup",
                    200,
                    "Picture repository game test setup")
            );
            isInitialized = true;
            startPicture = TestUtils.getUniqueTestPicture(game);
        }
    }

    @Test
    public void saveTest() throws Exception {
        Picture saved = pictureRepository.save(startPicture);
        Assertions.assertTrue(saved.equals(startPicture));
    }

    @Test
    public void findByIdTest() throws Exception {
        game = gameRepository.save(game);
        Picture picToSave = TestUtils.getUniqueTestPicture(game);
        Picture saved = pictureRepository.save(picToSave);
        Picture result = pictureRepository.findById(saved.getId()).get();
        Assertions.assertTrue(result.equals(picToSave));
    }
}
