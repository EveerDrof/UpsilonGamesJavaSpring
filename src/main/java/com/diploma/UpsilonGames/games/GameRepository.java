package com.diploma.UpsilonGames.games;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByName(String name);
    ArrayList<Game> findAll();
}
