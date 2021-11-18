package com.diploma.UpsilonGames.users;

import com.diploma.UpsilonGames.PasswordUtils;
import com.diploma.UpsilonGames.marks.Mark;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User {
    private static int passwordMinLength = 10;
    public static int getPasswordMinLength(){
        return passwordMinLength;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true,nullable = false)
    private String name;
    @Column(unique=true,nullable = false)
    private String password;

    @OneToMany(targetEntity= Mark.class,mappedBy = "userId",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

    public User() {
    }

    public User(String name,String password) throws IncorrectPasswordException {
        if(password.length()<passwordMinLength){
            throw new IncorrectPasswordException("Password is too short.It must have at least 10 symbols",new Exception());
        }
        if(!password.matches(".*\\d.*")){
            throw new IncorrectPasswordException("Password has no digits.It must have at least 1 digit",new Exception());
        }
        Pattern pattern = Pattern.compile("[^a-z0-9 ]",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.find()){
            throw new IncorrectPasswordException("Password has no special symbols." +
                "It must contain at least one of the following: !_-'\"+=()%$#@~` ",new Exception());
        }
        pattern = Pattern.compile("[A-Z]");
        matcher = pattern.matcher(password);
        if(!matcher.find()){
            throw new IncorrectPasswordException("Password has no capital letters." +
                "It must contain at least one capital letter",new Exception());
        }
        pattern = Pattern.compile("[a-z]");
        matcher = pattern.matcher(password);
        if(!matcher.find()){
            throw new IncorrectPasswordException("Password has no small letters." +
                "It must contain at least one small letter",new Exception());
        }
        this.name = name;
        this.password = PasswordUtils.hash(password.toCharArray());
    }

    public User(long id, String name,String password) throws IncorrectPasswordException {
        this(name,password);
        this.id = id;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return  false;
        }
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

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
