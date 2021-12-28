package com.diploma.UpsilonGames.votes;

import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private VoteRepository voteRepository;
    @Autowired
    public VoteService(VoteRepository voteRepository){

        this.voteRepository = voteRepository;
    }
    public long getReviewLikesNumber(Review reviewId){
        return voteRepository.getReviewLikesNumber(reviewId);
    }
    public long getReviewDislikesNumber(Review reviewId){
        return voteRepository.getReviewDislikesNumber(reviewId);
    }
    public void save(Vote vote){
        voteRepository.save(vote);
    }
    public Vote findByReviewAndUser(Review reviewId,User userId){
        return voteRepository.findByReviewIdAndUserId(reviewId,userId);
    }

    public long existsByReviewIdAndUserId(Review reviewId, User userId) {
        return voteRepository.existsByReviewIdByUserId(reviewId,userId);
    }
}
