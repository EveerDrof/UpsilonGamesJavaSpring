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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
                            StoreRecordService storeRecordService) {

        this.reviewService = reviewService;
        this.userService = userService;
        this.gameService = gameService;
        this.voteService = voteService;
        this.storeRecordService = storeRecordService;
        objectMapper = new ObjectMapper();
    }

    private HashMap<String, Object> reviewToHashMapWithAdditionalData(Review review, User user) {
        HashMap<String, Object> hashMap = objectMapper.convertValue(review, HashMap.class);
        hashMap.put("likesNumber", voteService.getReviewLikesNumber(review));
        hashMap.put("dislikesNumber", voteService.getReviewDislikesNumber(review));
        hashMap.put("isLiked", voteService.checkIfUserVoted(review, user, true));
        hashMap.put("isDisliked", voteService.checkIfUserVoted(review, user, false));
        return hashMap;
    }

    private ArrayList<HashMap<String, Object>> convertReviewsAndLoadAdditionalData(
            ArrayList<Review> reviews, User user) {
        return new ArrayList(reviews.stream()
                .map(review -> reviewToHashMapWithAdditionalData(review, user))
                .collect(Collectors.toList()));
    }

    public Object getReviews(String gameName, String sort, long reviewsNumber, User loggedUser, long neededUserId) {
        User neededUser;
        if (!userService.existsById(neededUserId)) {
            neededUser = null;
        } else {
            neededUser = userService.findById(neededUserId);
        }
        if (gameName == "") {
            if (neededUser == null) {
                return convertReviewsAndLoadAdditionalData(
                        reviewService.findAll(sort, reviewsNumber), loggedUser
                );
            }
            return convertReviewsAndLoadAdditionalData(
                    reviewService.findByUserId(sort, reviewsNumber, neededUser), loggedUser
            );
        }
        if (neededUser == null) {
            Game game = gameService.findByName(gameName);
            return convertReviewsAndLoadAdditionalData(
                    reviewService.findByGameId(sort, reviewsNumber, game)
                    , loggedUser);
        }
        Game game = gameService.findByName(gameName);
        return convertReviewsAndLoadAdditionalData(new ArrayList(List.of(
                reviewService.findByGameIdByUserId(game, neededUser))), loggedUser);
    }

    @GetMapping("/unauthorized")
    public Object getReviewsUnauthorized(@RequestParam String gameName,
                                         @RequestParam String sort, @RequestParam long reviewsNumber,
                                         @RequestParam long neededUserId) {
        return getReviews(gameName, sort, reviewsNumber, null, neededUserId);
    }

    @GetMapping("/authorized")
    public Object getReviewsAuthorized(@RequestParam String gameName,
                                       @RequestParam String sort, @RequestParam long reviewsNumber,
                                       @RequestParam long neededUserId,
                                       Principal principal) {
        User loggedUser;
        if (!userService.existsByName(principal.getName())) {
            loggedUser = null;
        } else {
            loggedUser = userService.findByName(principal.getName());
        }
        return getReviews(gameName, sort, reviewsNumber, loggedUser, neededUserId);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity postReview(Principal principal, @RequestParam String gameName,
                                     HttpEntity<String> httpEntity) {
        try {
            User user = userService.findByName(principal.getName());
            Game game = gameService.findByName(gameName);
            if (!storeRecordService.existsByGameIdAndUserIdAndType(game, user,
                    StoreRecordType.IN_LIBRARY)) {
                return new ResponseEntity("Game is not in your library",
                        HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
            }
            Review createdReview = reviewService.findByGameIdByUserId(game, user);
            if (createdReview != null) {
                createdReview.setText(httpEntity.getBody());
                reviewService.save(createdReview);
            } else {
                reviewService.save(new Review(httpEntity.getBody(), game, user));
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
