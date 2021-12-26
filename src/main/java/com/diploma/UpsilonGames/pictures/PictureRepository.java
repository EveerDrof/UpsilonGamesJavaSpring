package com.diploma.UpsilonGames.pictures;

import com.diploma.UpsilonGames.games.Game;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    ArrayList<Picture> findAll();
    ArrayList<Picture> findByGameId(Game gameId);
}
