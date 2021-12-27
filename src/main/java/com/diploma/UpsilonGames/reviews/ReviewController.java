package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameService;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping(value = "reviews")
public class ReviewController {
    private ReviewService reviewService;
    private UserService userService;
    private GameService gameService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService, GameService gameService){

        this.reviewService = reviewService;
        this.userService = userService;
        this.gameService = gameService;
    }
    @GetMapping
    public ArrayList<Review> getReviews(@RequestParam long userId, @RequestParam String gameName,
                                        @RequestParam String sort,@RequestParam long reviewsNumber){
        if(gameName == ""){
            if(userId == -1){
              return  reviewService.findAll(sort,reviewsNumber);
            }
            User user = userService.findById(userId);
            return reviewService.findByUserId(sort,reviewsNumber,user);
        }
        if(userId == -1){
            Game game = gameService.findByName(gameName);
            return  reviewService.findByGameId(sort,reviewsNumber,game);
        }
        Game game = gameService.findByName(gameName);
        User user = userService.findById(userId);
        return new ArrayList<Review>(Arrays.asList(reviewService.findByGameIdByUserId(game,user)));
    }
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity postReview(Principal principal, @RequestParam String gameName,
                                     HttpEntity<String> httpEntity)
    {
        try {
            User user = userService.findByName(principal.getName());
            Game game = gameService.findByName(gameName);
            Review createdReview = reviewService.findByGameIdByUserId(game,user);
            if(createdReview != null){
                createdReview.setReviewText(httpEntity.getBody());
                reviewService.save(createdReview);
            } else {
                reviewService.save(new Review(httpEntity.getBody(), game, user));
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
