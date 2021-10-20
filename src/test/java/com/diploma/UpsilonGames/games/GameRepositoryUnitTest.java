package com.diploma.UpsilonGames.games;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryUnitTest {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Test
    public void saveTest(){
        Game game = testEntityManager.persistFlushFind(new Game("Half life 2",400));
        Game found = gameRepository.getById(game.getId());
        Assertions.assertEquals(found.getId(),game.getId());
    }
    @Test
    public void getByNameTest(){
        Game game = testEntityManager.persistFlushFind(new Game("Far cry 4",400));
        Game found = gameRepository.findByName(game.getName());
        Assertions.assertEquals(game.getName(),found.getName());
    }
    @Test
    public void getInvalidGame_shouldReturnNull(){
        Game game = gameRepository.findByName("efffffdafadsf");
        Assertions.assertEquals(game,null);
    }
}
