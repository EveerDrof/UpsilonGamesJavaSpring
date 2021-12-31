package com.diploma.UpsilonGames.reviews;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.votes.Vote;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "userId", "gameId" } ) } )
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 9999)
    private String reviewText;
    @ManyToOne()
    @JoinColumn(name = "gameId",referencedColumnName = "id", updatable = false,nullable = false)
    private Game gameId;
    @ManyToOne()
    @JoinColumn(name = "userId",referencedColumnName = "id", updatable = false,nullable = false)
    private User userId;
    @OneToMany(targetEntity = Vote.class, mappedBy = "reviewId", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();
    private Timestamp creationDate;
    public Review(){

    }
    public Review(String reviewText,Game gameId,User userId){
        this.reviewText = reviewText;
        this.gameId = gameId;
        this.userId = userId;
        this.creationDate = new Timestamp(new Date().getTime());
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
