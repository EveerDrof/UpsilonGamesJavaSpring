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

    @Query(value = " SELECT DISTINCT(g.id),g.*,(SELECT IFNULL((SELECT AVG(m.mark) " +
            " FROM mark m WHERE g.id=m.game_id),-1)) as avg_mark " +
            " FROM game g " +
            " INNER JOIN game_tags gt ON g.id=gt.games_id INNER JOIN tag t ON " +
            " t.id = gt.tags_id LEFT JOIN mark m ON g.id = m.game_id WHERE t.name IN ?1 AND " +
            " g.discount_price <= ?2 AND g.price >= ?3 AND " +
            " g.name LIKE %?5% AND " +
            " (((g.price-g.discount_price)/g.price)*100) >= ?6 " +
            " GROUP BY g.id " +
            " HAVING COUNT(DISTINCT gt.tags_id) = ?7 AND avg_mark >= ?4  ORDER BY avg_mark DESC " +
            " LIMIT ?8",
            nativeQuery = true)
    ArrayList<Game> select(String[] tagsArr, double maxPrice, double minPrice, byte minMark,
                           String namePart, double minDiscountPercent, int tagsArrLength,
                           int limit);

    @Query(value = " SELECT DISTINCT(g.id),g.*, (SELECT IFNULL((SELECT AVG(m.mark) " +
            " FROM mark m WHERE g.id=m.game_id),-1)) AS avg_mark " +
            " FROM game g INNER JOIN game_tags gt ON g.id=gt.games_id INNER JOIN tag t ON " +
            " t.id = gt.tags_id LEFT JOIN mark m ON g.id = m.game_id WHERE " +
            " g.discount_price <= ?1 AND g.price >= ?2 AND " +
            " g.name LIKE %?4%  AND " +
            " (((g.price-g.discount_price)/g.price)*100) >= ?5 " +
            " HAVING avg_mark >= ?3 ORDER BY avg_mark DESC" +
            " LIMIT ?6",
            nativeQuery = true)
    ArrayList<Game> select(double maxPrice, double minPrice, byte minMark,
                           String namePart, double minDiscountPercent,
                           int limit);
}
