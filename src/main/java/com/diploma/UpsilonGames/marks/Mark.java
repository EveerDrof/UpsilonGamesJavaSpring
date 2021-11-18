package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint( columnNames = { "userId", "gameId" } ) } )
public class Mark {
    private byte mark;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "id", updatable = false,nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User userId;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "gameId", referencedColumnName = "id", updatable = false,nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Game gameId;

    public Mark() {
    }

    public Mark(byte mark, Game gameId, User userId) {
        this.mark = mark;
        this.gameId = gameId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mark mark = (Mark) o;

        if(!Objects.equals(userId, mark.userId))
            return false;
        return Objects.equals(gameId, mark.gameId);
    }
    public byte getMark() {
        return mark;
    }

    public long getId() {
        return id;
    }

    public Game getGameId() {
        return gameId;
    }

    public User getUserId() {
        return userId;
    }

    public void setMark(byte mark) {
        this.mark = mark;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }
}