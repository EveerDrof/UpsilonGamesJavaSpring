package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark,Long> {
    Mark getByUserIdAndGameId(User userId, Game gameId);
    List<Mark> findAllByUserId(User userId);
    List<Mark> findAllByGameId(Game gameId);
}
