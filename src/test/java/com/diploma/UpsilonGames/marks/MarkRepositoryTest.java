package com.diploma.UpsilonGames.marks;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    private Mark secondSavedMark;
    private Mark markWithAnotherGameid;
    private Mark markWithAnotherUserId;
    @BeforeEach
    public void setUp(){
        savedMark = testEntityManager.persistFlushFind(new Mark((byte)100,1,1));
        secondSavedMark = testEntityManager.persistFlushFind(new Mark((byte)100,1,1));
        markWithAnotherGameid = testEntityManager.persistFlushFind(new Mark((byte)100,10,1));
        markWithAnotherUserId = testEntityManager.persistFlushFind(new Mark((byte) 100,1,10));
    }
    @Test
    public void saveAndGetByUserIdAndGameIdTest(){
        Mark found = markRepository.getByUserIdAndByGameId(savedMark.getUserId(),savedMark.getGameId());
        Assertions.assertTrue(found.equals(savedMark));
    }
    @Test
    public void saveAndGetByUserIdTest(){
        List<Mark> foundMarks = markRepository.findAllByUserId(savedMark.getUserId());
        Assertions.assertTrue(foundMarks.get(0).equals(savedMark));
        Assertions.assertTrue(foundMarks.get(1).equals(secondSavedMark));
        Assertions.assertTrue(!foundMarks.contains(markWithAnotherUserId));
    }
    @Test
    public void saveAndGetByGameIdTest(){
        List<Mark> foundMarks = markRepository.findAllByGameId(savedMark.getUserId());
        Assertions.assertTrue(foundMarks.get(0).equals(savedMark));
        Assertions.assertTrue(foundMarks.get(1).equals(secondSavedMark));
        Assertions.assertTrue(!foundMarks.contains(markWithAnotherGameid));
    }
}
