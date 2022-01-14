package com.diploma.UpsilonGames.comments;

import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.votes.Vote;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Review review;
    @ManyToOne(optional = true)
    private Comment parent;
    @ManyToOne(optional = true)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User userId;
    private Timestamp creationDate;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
    @OneToMany(targetEntity = Vote.class, mappedBy = "commentId", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    public Comment() {

    }

    public Comment(String text, User user, Review review, Comment parent) {
        this.text = text;
        this.userId = user;
        this.review = review;
        this.parent = parent;
        this.creationDate = new Timestamp(new Date().getTime());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public String getUserName() {
        return userId.getName();
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return id;
    }

    public User getUserId() {
        return userId;
    }

}
