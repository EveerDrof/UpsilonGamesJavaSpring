package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository){

        this.reviewRepository = reviewRepository;
    }

    public ArrayList<Review> findAll(String sort, long reviewsNumber) {
        ArrayList<Review> reviews = null;
        switch (sort){
            case "newest":
                reviews = reviewRepository.findAll(reviewsNumber);
                break;
        }
        return  reviews;
    }

    public ArrayList<Review> findByUserId(String sort, long reviewsNumber, User userId) {
        ArrayList<Review> reviews = null;
        switch (sort){
            case "newest":
                reviews = reviewRepository.findByUserId(reviewsNumber,userId);
                break;
        }
        return  reviews;
    }
    public ArrayList<Review> findByGameId(String sort, long reviewsNumber, Game gameId) {
        ArrayList<Review> reviews = null;
        switch (sort){
            case "newest":
                reviews = reviewRepository.findByGameId(reviewsNumber,gameId);
                break;
        }
        return  reviews;
    }

    public Review findByGameIdByUserId( Game game, User user) {
        return reviewRepository.findByGameIdByUserId(game,user);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }
    public  Review findById(long reviewId){
        return reviewRepository.getById(reviewId);
    }

    public boolean existsById(long reviewId) {
        return reviewRepository.existsById(reviewId);
    }
}
