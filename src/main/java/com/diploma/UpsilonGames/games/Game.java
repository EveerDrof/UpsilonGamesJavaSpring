package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.pictures.Picture;
import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.tags.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @OneToMany(targetEntity= Mark.class,mappedBy = "gameId",cascade = CascadeType.ALL)
    private List<Mark> marks = new ArrayList<>();

    @OneToMany(targetEntity= Picture.class, mappedBy="gameId",cascade=CascadeType.ALL)
    private List<Picture> picturess = new ArrayList<>();

    @OneToMany(targetEntity= Review.class, mappedBy="gameId",cascade=CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne
    private Picture shortcut;
    public Game() {
    }
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable
    private List<Tag> tags;
    public Game(String name, double price, String description) {
        this.tags = new ArrayList<>();
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public void addTag(Tag tag){
        this.tags.add(tag);
    }
}
