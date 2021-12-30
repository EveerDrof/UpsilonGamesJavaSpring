package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameService;
import com.diploma.UpsilonGames.storeRecords.StoreRecordService;
import com.diploma.UpsilonGames.storeRecords.StoreRecordType;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserService;
import com.diploma.UpsilonGames.votes.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "reviews")
public class ReviewController {
    private ReviewService reviewService;
    private UserService userService;
    private GameService gameService;
    private VoteService voteService;
    private StoreRecordService storeRecordService;
    private ObjectMapper objectMapper;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService,
                            GameService gameService, VoteService voteService,
                            StoreRecordService storeRecordService){

        this.reviewService = reviewService;
        this.userService = userService;
        this.gameService = gameService;
        this.voteService = voteService;
        this.storeRecordService = storeRecordService;
        objectMapper = new ObjectMapper();
    }
    private HashMap<String,Object> reviewToHashMapWithAdditionalData(Review review){
        HashMap<String,Object> hashMap = objectMapper.convertValue(review, HashMap.class);
        hashMap.put("likesNumber",voteService.getReviewLikesNumber(review));
        hashMap.put("dislikesNumber",voteService.getReviewDislikesNumber(review));
        return hashMap;
    }
    private ArrayList<HashMap<String,Object>> convertReviewsAndLoadAdditionalData(
            ArrayList<Review> reviews){
        return new ArrayList(reviews.stream()
                .map(review -> reviewToHashMapWithAdditionalData(review))
                .collect(Collectors.toList()));
    }
    @GetMapping
    public Object getReviews(@RequestParam long userId, @RequestParam String gameName,
                                   @RequestParam String sort, @RequestParam long reviewsNumber){
        if(gameName == ""){
            if(userId == -1){
              return  convertReviewsAndLoadAdditionalData(
                      reviewService.findAll(sort,reviewsNumber)
              );
            }
            User user = userService.findById(userId);
            return convertReviewsAndLoadAdditionalData(
                    reviewService.findByUserId(sort,reviewsNumber,user)
            );
        }
        if(userId == -1){
            Game game = gameService.findByName(gameName);
            return  convertReviewsAndLoadAdditionalData(
                    reviewService.findByGameId(sort,reviewsNumber,game)
            );
        }
        Game game = gameService.findByName(gameName);
        User user = userService.findById(userId);
        return convertReviewsAndLoadAdditionalData(new ArrayList(List.of(
                reviewService.findByGameIdByUserId(game,user))));
    }
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity postReview(Principal principal, @RequestParam String gameName,
                                     HttpEntity<String> httpEntity)
    {
        try {
            User user = userService.findByName(principal.getName());
            Game game = gameService.findByName(gameName);
            if(!storeRecordService.existsByGameIdAndUserIdAndType(game,user,
                    StoreRecordType.IN_LIBRARY)){
                return new ResponseEntity("Game is not in your library",
                        HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
            }
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
