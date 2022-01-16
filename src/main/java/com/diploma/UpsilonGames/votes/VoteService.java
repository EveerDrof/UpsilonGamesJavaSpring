package com.diploma.UpsilonGames.votes;

import com.diploma.UpsilonGames.comments.Comment;
import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {

        this.voteRepository = voteRepository;
    }

    public long getReviewLikesNumber(Review reviewId) {
        return voteRepository.getReviewLikesNumber(reviewId);
    }

    public long getReviewDislikesNumber(Review reviewId) {
        return voteRepository.getReviewDislikesNumber(reviewId);
    }

    public void save(Vote vote) {
        voteRepository.save(vote);
    }

    public Vote findByReviewAndUser(Review reviewId, User userId) {
        return voteRepository.findByReviewIdAndUserId(reviewId, userId);
    }

    public long existsByReviewIdAndUserId(Review reviewId, User userId) {
        return voteRepository.existsByReviewIdByUserId(reviewId, userId);
    }

    public long getCommentLikesNumber(Comment c) {
        return voteRepository.getCommentLikesNumber(c);
    }

    public long getCommentDislikesNumber(Comment c) {
        return voteRepository.getCommentDislikesNumber(c);
    }

    public boolean checkIfUserVoted(Comment comment, User user, boolean voteType) {
        if (user == null) {
            return false;
        }
        return voteRepository.checkIfUserVoted(comment, user, voteType) == 1;
    }

    public boolean checkIfUserVoted(Review review, User user, boolean voteType) {
        if (user == null) {
            return false;
        }
        return voteRepository.checkIfUserVoted(review, user, voteType) == 1;
    }
}
