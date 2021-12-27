package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.pictures.Picture;
import com.diploma.UpsilonGames.reviews.Review;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double price;
    private String description;

    public Game(String name, double price) {
        this(name,price,"");
    }

    @OneToMany(targetEntity= Mark.class,mappedBy = "gameId",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

    @OneToMany(targetEntity= Picture.class, mappedBy="gameId",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Picture> picturess = new ArrayList<>();

    @OneToMany(targetEntity= Review.class, mappedBy="gameId",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne
    private Picture shortcut;
    public Game() {
    }

    public Game(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Game(long id, String name, double price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }
    public Game(String name, double price, String description,Picture shortcut) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.shortcut = shortcut;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setShortcut(Picture shortcut){
        this.shortcut = shortcut;
    }

    public Picture getShortcut() {
        return shortcut;
    }
}
