package com.diploma.UpsilonGames.marks;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MarkId implements Serializable {
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id",table = "user")
    private User userId;
    @OneToOne
    @JoinColumn(name = "gameId", referencedColumnName = "id",table = "game")
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
