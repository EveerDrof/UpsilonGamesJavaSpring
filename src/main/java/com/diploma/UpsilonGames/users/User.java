package com.diploma.UpsilonGames.users;

import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.marks.MarkId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true,nullable = false)
    private String name;

    @OneToMany(targetEntity= Mark.class,mappedBy = "userId",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

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
