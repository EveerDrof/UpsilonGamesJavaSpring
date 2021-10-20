package com.diploma.UpsilonGames.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game findGameByName(String name) {
        return gameRepository.findByName(name);
    }

    public Game save(Game game) {
       return gameRepository.save(game);
    }
}
