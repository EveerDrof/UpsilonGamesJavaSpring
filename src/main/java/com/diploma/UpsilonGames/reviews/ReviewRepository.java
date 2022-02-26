package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(nativeQuery = true,value = "SELECT * FROM review r ORDER BY r.creation_date DESC  LIMIT ?1")
    ArrayList<Review> findAllNewest(long reviewsNumber);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.user_id = ?2 LIMIT ?1")
    ArrayList<Review> findByUserId(long reviewsNumber, User userId);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.game_id = ?2 " +
            " ORDER BY r.creation_date DESC LIMIT ?1 ")
    ArrayList<Review> findByGameIdNewest(long reviewsNumber, Game gameId);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.game_id = ?1 AND r.user_id = ?2")
    Review findByGameIdByUserId(Game gameId,User userId);

    @Query(nativeQuery = true,value = "SELECT * FROM review r ORDER BY r.creation_date ASC  LIMIT ?1 ")
    ArrayList<Review> findAllOldest(long reviewsNumber);
    @Query(nativeQuery = true,value = "SELECT * FROM review r WHERE r.game_id = ?2" +
            " ORDER BY r.creation_date ASC  LIMIT ?1 ")
    ArrayList<Review> findByGameIdOldest(long reviewsNumber, Game gameId);


    @Query(nativeQuery = true,value = "SELECT r.*, COUNT(CASE WHEN v.vote = true THEN 1 END) " +
            " as number_of_likes FROM review r " +
            " LEFT JOIN vote v ON (v.review_id=r.id) WHERE r.game_id = ?2 GROUP BY r.id " +
            " ORDER BY number_of_likes DESC LIMIT ?1")
    ArrayList<Review> findByGameIdMostLiked(long reviewsNumber, Game gameId);
    @Query(value = "SELECT r.*, " +
            " COUNT(CASE WHEN v.vote = true THEN 1 END) - COUNT(CASE WHEN v.vote =false THEN 1 END) AS difference " +
            " FROM review r LEFT JOIN " +
            " vote v ON (v.review_id=r.id) WHERE r.game_id =?2 GROUP BY r.id ORDER BY difference DESC " +
            " LIMIT ?1 ",nativeQuery = true)
    ArrayList<Review> findByGameIdWithHighestDifference(long reviewsNumber, Game gameId);
}
