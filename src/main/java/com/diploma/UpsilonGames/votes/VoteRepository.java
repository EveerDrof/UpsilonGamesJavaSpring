package com.diploma.UpsilonGames.votes;

import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    @Query(value = "SELECT COUNT(*)  FROM vote v WHERE v.review_id = ?1 AND v.vote = true",
            nativeQuery = true)
    long getReviewLikesNumber(Review reviewId);
    @Query(value = "SELECT COUNT(*)  FROM vote v WHERE v.review_id = ?1 AND v.vote = false",
            nativeQuery = true)
    long getReviewDislikesNumber(Review reviewId);
    @Query(value = "SELECT * FROM vote v WHERE v.review_id = ?1 AND v.user_id = ?2",
            nativeQuery = true)
    Vote findByReviewIdAndUserId(Review reviewId, User userId);
    @Query(value = "SELECT EXISTS(SELECT * FROM vote v WHERE v.review_id = ?1 AND v.user_id = ?2)"
            ,nativeQuery = true)
    long existsByReviewIdByUserId(Review reviewId, User userId);
}
