package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;

import java.util.List;

public class MarkService {
    private MarkRepository markRepository;
    public MarkService(MarkRepository markRepository){
        this.markRepository = markRepository;
    }
    public Mark getMark(long userId,long gameId){
        return null;
    }
    public List<Mark> findAllByUserId(User userId){
        return markRepository.findAllByUserId(userId);
    }
    public List<Mark> findAllByGameId(Game gameId){
        return markRepository.findAllByGameId(gameId);
    }
}
