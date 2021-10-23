package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MarkId implements Serializable {
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", referencedColumnName = "id",table = "user", updatable = false)
    private User userId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "gameId", referencedColumnName = "id",table = "game", updatable = false)
    private Game gameId;
    @Override

    public boolean  equals(Object o){
        if(this == o){
            return true;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        MarkId markId = (MarkId)o;
        if(!Objects.equals(this.gameId,markId.gameId)){
            return false;
        }
        return Objects.equals(this.userId,markId.userId);
    }

    public long getUserId() {
        return userId.getId();
    }

    public long getGameId() {
        return gameId.getId();
    }
}
