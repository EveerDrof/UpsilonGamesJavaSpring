package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.IMarkAcceptableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IMarkAcceptableService {
    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game findByName(String name) {
        return gameRepository.findByName(name);
    }

    public Game save(Game game) {
       return gameRepository.save(game);
    }
    public boolean existsById(long gameId){
        return gameRepository.existsById(gameId);
    }
    public Game findById(long id){
        return gameRepository.getById(id);
    }
}
