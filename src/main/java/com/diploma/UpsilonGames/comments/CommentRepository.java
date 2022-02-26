package com.diploma.UpsilonGames.comments;

import com.diploma.UpsilonGames.reviews.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comment c WHERE review_id  = ?1 AND parent_id IS NULL " +
            " LIMIT ?2", nativeQuery = true)
    ArrayList<Comment> getReviewComments(Review reviewId, long commentsNumber);
}
