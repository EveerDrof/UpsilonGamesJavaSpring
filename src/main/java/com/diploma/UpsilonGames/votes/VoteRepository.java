package com.diploma.UpsilonGames.votes;

import com.diploma.UpsilonGames.comments.Comment;
import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Long> {
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
            , nativeQuery = true)
    long existsByReviewIdByUserId(Review reviewId, User userId);

    @Query(value = "SELECT COUNT(*) FROM vote v WHERE v.comment_id = ?1 AND v.vote = true",
            nativeQuery = true)
    long getCommentLikesNumber(Comment c);

    @Query(value = "SELECT COUNT(*) FROM vote v WHERE v.comment_id = ?1 AND v.vote = false",
            nativeQuery = true)
    long getCommentDislikesNumber(Comment c);

    @Query(value = "SELECT EXISTS(SELECT * FROM vote v WHERE v.comment_id= ?1 AND v.user_id = ?2 " +
            " AND v.vote = ?3)",
            nativeQuery = true)
    long checkIfUserVoted(Comment comment, User user, Boolean voteType);

    @Query(value = "SELECT EXISTS(SELECT * FROM vote v WHERE v.review_id= ?1 AND v.user_id = ?2 " +
            " AND v.vote = ?3)",
            nativeQuery = true)
    long checkIfUserVoted(Review review, User user, Boolean voteType);
}
