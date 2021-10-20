package com.diploma.UpsilonGames.users;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true,nullable = false)
    private String name;

    public User() {
    }
    public User(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object o){
        if(this == o) {
            return true;
        }
        if(this.getClass() != o.getClass()){
            return false;
        }
        User user = (User)o;
        return Objects.equals(this.name,user.name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
