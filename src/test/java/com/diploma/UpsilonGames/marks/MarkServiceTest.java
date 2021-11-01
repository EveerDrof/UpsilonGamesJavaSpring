package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MarkServiceTest {
    private MarkService markService;
    @Mock
    private MarkRepository markRepository;

    private Game gameFirst;
    private User userFirst;
    private Game gameSecond;
    private User userSecond;
    private byte markFirst = 100;
    private byte markSecond = 50;
    private List<Mark> marks;

    @BeforeEach
    public void setUp() throws Exception {
        gameFirst = new Game("game name", 2000, "descr");
        userFirst = new User("user name","2!Aaasdfafdaasdadssd");
        gameSecond = new Game("game name second", 2000, "descr 2");
        userSecond = new User("user name 2","2!Aaasdfafdaasdadssd");
        markService = new MarkService(markRepository);
        marks = List.of(
                new Mark(markFirst, gameFirst, userFirst),
                new Mark(markFirst, gameSecond, userFirst),
                new Mark(markFirst, gameFirst, userSecond),
                new Mark(markSecond, gameSecond, userSecond)
        );
    }

    @Test
    public void getByUserId_shouldReturnMarks() {
        given(markRepository.findAllByUserId(userFirst)).willReturn(marks.stream().filter((mark) -> {
            return Objects.equals(mark.getUserId(), userFirst);
        }).toList());
        List<Mark> foundMarks = markService.findAllByUserId(userFirst);
        Assertions.assertEquals(2, foundMarks.size());
        Assertions.assertTrue(Objects.equals(marks.get(0),foundMarks.get(0)));
        Assertions.assertTrue(Objects.equals(marks.get(1), foundMarks.get(1)));
    }
    @Test
    public void getByGameId_shouldReturnMarks() {
        given(markRepository.findAllByGameId(gameFirst)).willReturn(marks.stream().filter((mark) -> {
            return Objects.equals(mark.getGameId(), gameFirst);
        }).toList());
        List<Mark> foundMarks = markService.findAllByGameId(gameFirst);
        Assertions.assertEquals(2, foundMarks.size());
        Assertions.assertTrue(Objects.equals(marks.get(0),foundMarks.get(0)));
        Assertions.assertTrue(Objects.equals(marks.get(2), foundMarks.get(1)));
    }
    @Test
    public void addCorrectMark_shouldReturnMark(){
        Mark mark = marks.get(0);
        given(markRepository.save(mark)).willReturn(mark);
        Mark result = markService.save(mark);
        Assertions.assertTrue(mark.equals(result));
    }
    @Test
    public void getMarkByUserIdAndGameId_shouldReturnMark(){
        given(markRepository.findByUserIdAndGameId(userFirst,gameFirst)).willReturn(marks.get(0));
        byte result = markService.findMarkByUserIdAndGameId(userFirst,gameFirst);
        Assertions.assertEquals(result,marks.get(0).getMark());
    }
    @Test
    public void getAverageMarkByGameId(){
        byte expected = 22;
        given(markRepository.getAverageMarkByGameId(gameFirst)).willReturn(expected);
        byte result = markService.getAverageMarkByGameId(gameFirst);
        Assertions.assertEquals(expected,result);
    }
}
