package com.diploma.UpsilonGames.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByName(String name);
    ArrayList<Game> findAll();
    boolean existsByName(String name);
    @Query(value =  " SELECT DISTINCT(g.id),g.* " +
            " FROM game g INNER JOIN game_tags gt ON g.id=gt.games_id INNER JOIN tag t ON " +
            " t.id = gt.tags_id INNER JOIN mark m ON g.id = m.game_id WHERE t.name IN ?1 " +
            " AND g.price <= ?2 AND g.price >= ?3 AND (SELECT AVG(m.mark) FROM mark m WHERE " +
            " g.id=m.game_id) >= ?4 AND g.name LIKE %?5%",nativeQuery = true)
    ArrayList<Game> select(String[] tagsArr, double maxPrice, double minPrice, byte minMark,
                           String namePart);
    @Query(value =  " SELECT DISTINCT(g.id),g.* " +
            " FROM game g INNER JOIN game_tags gt ON g.id=gt.games_id INNER JOIN tag t ON " +
            " t.id = gt.tags_id INNER JOIN mark m ON g.id = m.game_id WHERE " +
            "  g.price <= ?1 AND g.price >= ?2 AND (SELECT AVG(m.mark) FROM mark m WHERE " +
            " g.id=m.game_id) >= ?3 AND g.name LIKE %?4%",nativeQuery = true)
    ArrayList<Game> select(double maxPrice, double minPrice, byte minMark,
                               String namePart);
}
