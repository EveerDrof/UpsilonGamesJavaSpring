package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark,Long> {
    Mark findByUserIdAndGameId(User userId, Game gameId);
    List<Mark> findAllByUserId(User userId);
    List<Mark> findAllByGameId(Game gameId);
    @Query(value = "SELECT AVG(mark) FROM mark WHERE game_id = ?1",nativeQuery = true)
    Byte getAverageMarkByGameId(Game gameId);
}
