package com.diploma.UpsilonGames.votes;

import com.diploma.UpsilonGames.comments.Comment;
import com.diploma.UpsilonGames.comments.CommentService;
import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.reviews.ReviewService;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "votes")
public class VoteController {
    private VoteService voteService;
    private UserService userService;
    private ReviewService reviewService;
    private CommentService commentService;

    @Autowired
    public VoteController(VoteService voteService, UserService userService,
                          ReviewService reviewService, CommentService commentService) {

        this.voteService = voteService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.commentService = commentService;
    }

    @PostMapping(value = "review")
    public ResponseEntity postReviewVote(Principal principal, @RequestParam boolean vote,
                                         @RequestParam long reviewId) {
        User user = userService.findByName(principal.getName());
        if (user == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Review review = reviewService.findById(reviewId);
        if (review == null) {
            return new ResponseEntity("Review not found", HttpStatus.NOT_FOUND);
        }
        try {
            voteService.save(new Vote(vote, user, review));
        } catch (Exception ex) {
            return new ResponseEntity("Incorrect data", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "review")
    public ResponseEntity getVote(@RequestParam long userId, @RequestParam long reviewId) {
        if (!userService.existsById(userId)) {
            return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
        }
        User user = userService.findById(userId);
        if (!reviewService.existsById(reviewId)) {
            return new ResponseEntity("Review not found", HttpStatus.BAD_REQUEST);
        }
        Review review = reviewService.findById(reviewId);
        if (voteService.existsByReviewIdAndUserId(review, user) == 0) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Vote vote = voteService.findByReviewAndUser(review, user);
        return new ResponseEntity(vote, HttpStatus.OK);
    }

    @PostMapping("/comment/{commentId}")
    public ResponseEntity addComment(@PathVariable long commentId,
                                     @RequestParam boolean voteType,
                                     Principal principal) {
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        if (!commentService.existsById(commentId)) {
            return new ResponseEntity("No such comment", HttpStatus.BAD_REQUEST);
        }
        Comment comment = commentService.findById(commentId);
        Vote vote = new Vote(voteType, user, comment);
        try {
            voteService.save(vote);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Duplicate vote", HttpStatus.BAD_REQUEST);
        }
    }
}
