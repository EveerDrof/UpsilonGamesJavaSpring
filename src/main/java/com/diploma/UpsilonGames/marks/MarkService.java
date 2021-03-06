package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkService {
    private MarkRepository markRepository;

    @Autowired
    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public List<Mark> findAllByUserId(User userId) {
        return markRepository.findAllByUserId(userId);
    }

    public List<Mark> findAllByGameId(Game gameId) {
        return markRepository.findAllByGameId(gameId);
    }

    public Mark save(Mark mark) {
        return markRepository.save(mark);
    }

    public byte findMarkByUserIdAndGameId(User user, Game game) {
        if (user == null) {
            return markRepository.getAverageMarkByGameId(game);
        }
        Mark mark = markRepository.findByUserIdAndGameId(user, game);
        return mark.getMark();
    }

    public byte getAverageMarkByGameId(Game gameId) {
        return markRepository.getAverageMarkByGameId(gameId);
    }
}
