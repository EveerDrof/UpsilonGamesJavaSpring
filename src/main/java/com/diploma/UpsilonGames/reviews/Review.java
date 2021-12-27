package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "userId", "gameId" } ) } )
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String reviewText;
    @ManyToOne()
    @JoinColumn(name = "gameId",referencedColumnName = "id", updatable = false,nullable = false)
    private Game gameId;
    @ManyToOne()
    @JoinColumn(name = "userId",referencedColumnName = "id", updatable = false,nullable = false)
    private User userId;
    public Review(){

    }
    public Review(String reviewText,Game gameId,User userId){
        this.reviewText = reviewText;
        this.gameId = gameId;
        this.userId = userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
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
