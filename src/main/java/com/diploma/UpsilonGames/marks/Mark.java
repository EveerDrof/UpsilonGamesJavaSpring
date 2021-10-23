package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;

import javax.persistence.*;
import java.util.Objects;

//@Entity
//@SecondaryTable(name = "game", pkJoinColumns =
//    @PrimaryKeyJoinColumn(name = "markId",referencedColumnName = "id"))
//@SecondaryTable(name = "user", pkJoinColumns =
//    @PrimaryKeyJoinColumn(name = "userId",referencedColumnName = "id"))
//public class Mark {
//    private byte mark;
//
//
//    public Mark() {
//    }
//
//    public Mark(byte mark, long gameId, int userId) {
//        this.mark = mark;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Mark mark = (Mark) o;
//
//        return Objects.equals(markId, mark.markId);
//    }
//    public int getMark() {
//        return mark;
//    }
//
//    public long getGameId() {
//        return markId.getGameId();
//    }
//
//    public long getUserId() {
//        return markId.getUserId();
//    }
//}

@Entity
public class Mark {
    private byte mark;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "id", updatable = false,nullable = false)
    private User userId;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "gameId", referencedColumnName = "id", updatable = false,nullable = false)
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
    public short getMark() {
        return mark;
    }

    public Game getGameId() {
        return gameId;
    }

    public User getUserId() {
        return userId;
    }
}