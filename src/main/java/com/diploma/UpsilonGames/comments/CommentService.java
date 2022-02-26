package com.diploma.UpsilonGames.comments;

import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.votes.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private VoteService voteService;

    @Autowired
    public CommentService(CommentRepository commentRepository, VoteService voteService) {

        this.commentRepository = commentRepository;
        this.voteService = voteService;
    }

    private void sortComments(List<Comment> comments, String sortType) {
        switch (sortType) {
            case "newest":
                comments.sort((a, b) -> {
                    return (int) (b.getCreationDate().getTime() - a.getCreationDate().getTime());
                });
                break;
            case "oldest":
                comments.sort((a, b) -> (int) (a.getCreationDate().getTime()
                        - b.getCreationDate().getTime()));
                break;
            case "mostLiked":
                comments.sort((a, b) -> (int) (voteService.getCommentLikesNumber(b) -
                        voteService.getCommentLikesNumber(a)));
                break;
            case "highestDifference":
                comments.sort((a, b) -> (int) ((voteService.getCommentLikesNumber(b) -
                        voteService.getCommentDislikesNumber(b)) -
                        (voteService.getCommentLikesNumber(a) -
                                voteService.getCommentDislikesNumber(a))));
                break;
        }
        for (Comment c : comments) {
            sortComments(c.getChildren(), sortType);
        }
    }

    public ArrayList<Comment> getReviewComments(Review reviewId, long commentsNumber, String sortType) {
        ArrayList<Comment> comments = commentRepository.getReviewComments(reviewId, commentsNumber);
        sortComments(comments, sortType);
        return comments;
    }

    public boolean existsById(long parentCommentId) {
        return commentRepository.existsById(parentCommentId);
    }

    public Comment findById(long commentId) {
        return commentRepository.findById(commentId).get();
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
