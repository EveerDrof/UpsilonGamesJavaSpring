package com.diploma.UpsilonGames.votes;

import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "userId", "reviewId" } ) } )
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean vote;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "userId",referencedColumnName = "id", updatable = false,nullable = false)
    private User userId;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "reviewId",referencedColumnName = "id", updatable = false)
    private Review reviewId;
    public Vote(){

    }
    public Vote(boolean vote,User userId,Review reviewId){
        this.vote = vote;
        this.userId = userId;
        this.reviewId = reviewId;
    }
    public boolean getVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
