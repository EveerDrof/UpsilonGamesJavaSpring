package com.diploma.UpsilonGames.pictures;


import com.diploma.UpsilonGames.games.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import javax.persistence.*;
import java.sql.Blob;

@Entity
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @NotNull
    @JsonIgnore
    private Blob data;

    @ManyToOne()
    @JoinColumn(name = "gameId",referencedColumnName = "id", updatable = false,nullable = false)
    private Game gameId;

    public Picture() {
    }

    public Picture(Blob data, Game game) {
        this.data = data;
        this.gameId  = game;
    }
    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(this == o){
            return true;
        }
        if( o.getClass()!=this.getClass()){
            return false;
        }
        Picture pic = (Picture) o;
        if(this.data.equals(pic.data)){
            return true;
        }
        return true;
    }

    public Blob getData() {
        return data;
    }

    public long getId() {
        return id;
    }
}
