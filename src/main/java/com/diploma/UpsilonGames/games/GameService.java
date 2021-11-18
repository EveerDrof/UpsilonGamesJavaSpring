package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.IMarkAcceptableService;
import com.diploma.UpsilonGames.marks.MarkRepository;
import com.diploma.UpsilonGames.pictures.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class GameService implements IMarkAcceptableService {
    private GameRepository gameRepository;
    private MarkRepository markRepository;
    @Autowired
    public GameService(@Lazy GameRepository gameRepository,@Lazy MarkRepository markRepository) {
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
        byte mark;
        try {
            mark = markRepository.getAverageMarkByGameId(gameId);
        } catch (Exception ex){
            mark = -1;
        }
        return mark;
    }

    public Picture getShortcutByGameName(String gameName) {
        return gameRepository.findByName(gameName).getShortcut();
    }

    public ArrayList<Game> findAll() {
        return gameRepository.findAll();
    }
}
