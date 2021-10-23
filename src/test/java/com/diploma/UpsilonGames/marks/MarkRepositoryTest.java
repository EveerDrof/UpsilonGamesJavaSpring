package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MarkRepositoryTest {
    @Autowired
    MarkRepository markRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private Mark savedMark;
    private Mark markWithAnotherGameid;
    private Mark markWithAnotherUserId;
    private Game firstGame;
    private Game secondGame;
    private User firstUser;
    private User secondUser;
    @BeforeEach
    public void setUp(){
        firstGame = testEntityManager.persistFlushFind(new Game("Crysis",2000));
        secondGame =  testEntityManager.persistFlushFind(new Game("Far cry",1000));
        firstUser =  testEntityManager.persistFlushFind(new User("Ivan"));
        secondUser = testEntityManager.persistFlushFind( new User("Peter"));
        savedMark = testEntityManager.persistFlushFind(new Mark((byte)100,firstGame,firstUser));
        markWithAnotherGameid = testEntityManager.persistFlushFind(new Mark((byte)100,secondGame,firstUser));
        markWithAnotherUserId = testEntityManager.persistFlushFind(new Mark((byte) 100,firstGame,secondUser));
    }
    @Test
    public void saveAndGetByUserIdAndGameIdTest(){
        Mark found = markRepository.getByUserIdAndGameId(savedMark.getUserId(),savedMark.getGameId());
        Assertions.assertTrue(found.equals(savedMark));
    }
    @Test
    public void saveAndGetByUserIdTest(){
        List<Mark> foundMarks = markRepository.findAllByUserId(savedMark.getUserId());
        Assertions.assertTrue(foundMarks.get(0).equals(savedMark));
        Assertions.assertTrue(!foundMarks.contains(markWithAnotherUserId));
    }
    @Test
    public void saveAndGetByGameIdTest(){
        List<Mark> foundMarks = markRepository.findAllByGameId(savedMark.getGameId());
        Assertions.assertTrue(foundMarks.get(0).equals(savedMark));
        Assertions.assertTrue(!foundMarks.contains(markWithAnotherGameid));
    }
    @Test
    public void saveTwoEquals(){
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> {
            markRepository.save(savedMark);
            markRepository.save(new Mark((byte) 100, savedMark.getGameId(), savedMark.getUserId()));
            Mark found = markRepository.getByUserIdAndGameId(savedMark.getUserId(), savedMark.getGameId());
        });
    }
}
