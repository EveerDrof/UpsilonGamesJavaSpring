package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(nativeQuery = true,value = "SELECT * FROM review LIMIT ?1")
    ArrayList<Review> findAll(long reviewsNumber);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.user_id = ?2 LIMIT ?1")
    ArrayList<Review> findByUserId(long reviewsNumber, User userId);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.game_id = ?2 LIMIT ?1")
    ArrayList<Review> findByGameId(long reviewsNumber, Game gameId);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.game_id = ?1 AND r.user_id = ?2")
    Review findByGameIdByUserId(Game gameId,User userId);
}
