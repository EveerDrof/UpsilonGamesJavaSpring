package com.diploma.UpsilonGames.comments;

import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.reviews.ReviewService;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserService;
import com.diploma.UpsilonGames.votes.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping(value = "comments")
public class CommentController {
    private CommentService commentService;
    private ReviewService reviewService;
    private UserService userService;
    private VoteService voteService;
    private ObjectMapper objectMapper;

    @Autowired
    public CommentController(CommentService commentService, ReviewService reviewService,
                             UserService userService, VoteService voteService) {

        this.commentService = commentService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.voteService = voteService;
        objectMapper = new ObjectMapper();
    }

    private HashMap<String, Object> commentToMapWithAdditionalData(Comment comment, User user) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", comment.getId());
        result.put("likesNumber", voteService.getCommentLikesNumber(comment));
        result.put("dislikesNumber", voteService.getCommentDislikesNumber(comment));
        result.put("text", comment.getText());
        result.put("creationDate", comment.getCreationDate().getTime());
        result.put("isLiked", voteService.checkIfUserVoted(comment, user, true));
        result.put("isDisliked", voteService.checkIfUserVoted(comment, user, false));
        result.put("userId", comment.getUserId());
        ArrayList<Object> children = new ArrayList<>();
        for (Comment child : comment.getChildren()) {
            children.add(commentToMapWithAdditionalData(child, user));
        }
        result.put("children", children);
        return result;
    }

    private ArrayList<HashMap<String, Object>> commentsArrToHashMapWithAdditionalData(
            ArrayList<Comment> comments, User user
    ) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        for (Comment c : comments) {
            result.add(commentToMapWithAdditionalData(c, user));
        }
        return result;
    }

    public ResponseEntity getReviewComments(long reviewId,
                                            long commentsNumber,
                                            String sort,
                                            User loggedUser) {
        if (!reviewService.existsById(reviewId)) {
            return new ResponseEntity("No such review", HttpStatus.BAD_REQUEST);
        }
        Review review = reviewService.findById(reviewId);

        return new ResponseEntity(commentsArrToHashMapWithAdditionalData(
                commentService.getReviewComments(review, commentsNumber, sort), loggedUser),
                HttpStatus.OK);
    }

    @GetMapping("/review/{reviewId}/unauthorized")
    public ResponseEntity getReviewCommentsUnauthorized(@PathVariable long reviewId,
                                                        @RequestParam long commentsNumber,
                                                        @RequestParam String sort) {
        return getReviewComments(reviewId, commentsNumber, sort, null);
    }

    @GetMapping("/review/{reviewId}/authorized")
    public ResponseEntity getReviewCommentsAuthorized(@PathVariable long reviewId,
                                                      @RequestParam long commentsNumber,
                                                      @RequestParam String sort,
                                                      Principal principal) {
        User loggedUser = null;
        if (principal != null) {
            loggedUser = userService.findByName(principal.getName());
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return getReviewComments(reviewId, commentsNumber, sort, loggedUser);
    }

    @PostMapping("/review/{reviewId}")
    public ResponseEntity postReviewComment(@PathVariable long reviewId,
                                            @RequestParam long parentCommentId,
                                            @RequestParam String text,
                                            Principal principal) {
        if (!userService.existsByName(principal.getName())) {
            return new ResponseEntity("No such user", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByName(principal.getName());
        if (!reviewService.existsById(reviewId)) {
            return new ResponseEntity("No such review", HttpStatus.BAD_REQUEST);
        }
        Review review = reviewService.findById(reviewId);
        Comment parentComment = null;
        if (parentCommentId != -1) {
            if (!commentService.existsById(parentCommentId)) {
                return new ResponseEntity("No such parent comment", HttpStatus.BAD_REQUEST);
            }
            parentComment = commentService.findById(parentCommentId);
        }
        commentService.save(new Comment(text, user, review, parentComment));
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
