package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.marks.MarkId;

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

    @OneToMany(targetEntity= Mark.class,mappedBy = "gameId",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

    public Game() {
    }

    public Game(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public long getId() {
        return id;
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
}
