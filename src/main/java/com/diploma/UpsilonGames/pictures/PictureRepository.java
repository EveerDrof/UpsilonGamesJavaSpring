package com.diploma.UpsilonGames.pictures;

import com.diploma.UpsilonGames.games.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    ArrayList<Picture> findAll();

    @Query(value = "SELECT * FROM picture p " +
            " INNER JOIN game g ON g.id = p.game_id " +
            " WHERE g.id = ?1 AND p.id != g.shortcut_id;", nativeQuery = true)
    ArrayList<Picture> findByGameId(Game gameId);
}
