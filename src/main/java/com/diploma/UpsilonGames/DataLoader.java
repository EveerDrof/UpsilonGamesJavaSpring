package com.diploma.UpsilonGames;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameRepository;
import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.marks.MarkRepository;
import com.diploma.UpsilonGames.security.UserRole;
import com.diploma.UpsilonGames.users.IncorrectPasswordException;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private MarkRepository markRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, GameRepository gameRepository, MarkRepository markRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.markRepository = markRepository;
    }

    public void run(ApplicationArguments args) throws IncorrectPasswordException {
        ArrayList<Game> games = new ArrayList<>(Arrays.asList(
                new Game("Stalker",500,"Game about Chernobyl"),
                new Game("Devil May Cry",500,"Game about demons"))
        );
        gameRepository.saveAll(games);
        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User( "admin","Univac00Eniac_1",UserRole.ADMIN),
                new User("qkql","12_Passwsdfgdord",UserRole.USER),
                new User("bob","Passwdsdfgsdfg_00",UserRole.USER)
        ));
        userRepository.saveAll(users);
        ArrayList<Mark> marks = new ArrayList<>(Arrays.asList(
                new Mark((byte) 50,games.get(0),users.get(1)),
                new Mark((byte) 25,games.get(1),users.get(1)),
                new Mark((byte) 50,games.get(0),users.get(2)),
                new Mark((byte) 100,games.get(1),users.get(2))
        ));
        markRepository.saveAll(marks);
    }
}