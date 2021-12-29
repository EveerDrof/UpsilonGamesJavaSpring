package com.diploma.UpsilonGames.users;

import com.diploma.UpsilonGames.PasswordUtils;
import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.security.UserRole;
import com.diploma.UpsilonGames.votes.Vote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User implements UserDetails {
    @Autowired
    @Transient
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    private static int passwordMinLength = 10;

    public static int getPasswordMinLength() {
        return passwordMinLength;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @JsonIgnore
    @Column(unique = true, nullable = false)
    private String password;
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('USER','ADMIN')")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToMany(targetEntity = Mark.class, mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Mark> marks = new ArrayList<>();
    @OneToMany(targetEntity = Review.class, mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(targetEntity = Vote.class, mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();
    public User() {
    }
    public String checkAndEncodePassword(String password) throws IncorrectPasswordException{
        if (password.length() < passwordMinLength) {
            throw new IncorrectPasswordException("Password is too short.It must have at least 10 symbols", new Exception());
        }
        if (!password.matches(".*\\d.*")) {
            throw new IncorrectPasswordException("Password has no digits.It must have at least 1 digit", new Exception());
        }
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new IncorrectPasswordException("Password has no special symbols." +
                    "It must contain at least one of the following: !_-'\"+=()%$#@~` ", new Exception());
        }
        pattern = Pattern.compile("[A-Z]");
        matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new IncorrectPasswordException("Password has no capital letters." +
                    "It must contain at least one capital letter", new Exception());
        }
        pattern = Pattern.compile("[a-z]");
        matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new IncorrectPasswordException("Password has no small letters." +
                    "It must contain at least one small letter", new Exception());
        }
        return encoder.encode(password);
    }
    public User(String name, String password) throws IncorrectPasswordException {

        this.name = name;
        this.password = checkAndEncodePassword(password);
        role = UserRole.USER;
    }

    public User(long id, String name, String password) throws IncorrectPasswordException {
        this(name, password);
        this.id = id;
    }

    public User(String name, String password, UserRole role) throws IncorrectPasswordException {
        this(name,password);
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(this.name, user.name);
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
