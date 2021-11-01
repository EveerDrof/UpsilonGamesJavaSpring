package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.IMarkAcceptableService;
import com.diploma.UpsilonGames.marks.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IMarkAcceptableService {
    private GameRepository gameRepository;
    private MarkRepository markRepository;
    @Autowired
    public GameService(GameRepository gameRepository,MarkRepository markRepository) {
        this.gameRepository = gameRepository;
        this.markRepository = markRepository;
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

    public long getAvgMarkByGameId(Game gameId) {
        return markRepository.getAverageMarkByGameId(gameId);
    }
}
