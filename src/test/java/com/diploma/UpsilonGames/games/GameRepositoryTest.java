package com.diploma.UpsilonGames.games;

import org.junit.jupiter.api.BeforeEach;
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
public class GameRepositoryTest {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private Game game;
    @BeforeEach
    public void setUp(){
        game = testEntityManager.persistFlushFind(new Game("Half life 2",400));
    }
    @Test
    public void saveTest(){
        Game found = gameRepository.getById(game.getId());
        Assertions.assertEquals(found.getId(),game.getId());
    }
    @Test
    public void getByNameTest(){
        Game found = gameRepository.findByName(game.getName());
        Assertions.assertEquals(game.getName(),found.getName());
    }
    @Test
    public void getInvalidGame_shouldReturnNull(){
        Game game = gameRepository.findByName("efffffdafadsf");
        Assertions.assertEquals(game,null);
    }
    @Test
    public void updateUser(){
        Game oldUser = gameRepository.findByName(game.getName());
        oldUser.setName("Aaaaaaa");
        gameRepository.save(oldUser);
        Game newUser = gameRepository.findByName(oldUser.getName());
        Assertions.assertEquals(oldUser.getName(),newUser.getName());
        Assertions.assertEquals(oldUser.getId(),newUser.getId());
    }
    @Test
    public void updateUserWithConstructorWithId() {
        Game newGame = new Game(game.getId(),"Nameasdfafd",1000,"aees");
        gameRepository.save(newGame);
        Assertions.assertEquals(newGame.getName(),gameRepository.getById(newGame.getId()).getName());
    }
}
