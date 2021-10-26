package com.diploma.UpsilonGames.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private User user;
    private User result;
    @BeforeEach
    public void setUp()throws Exception{
        user = new User("Michael","2!Aaasdfafdaasdadssd");
        result = userRepository.save(user);
    }
    @Test
    public void addAndGetUserById(){
        Assertions.assertTrue(user.equals(result));
        User foundByRepo = userRepository.getById(user.getId());
        Assertions.assertEquals(result,foundByRepo);
    }
    @Test
    public void getByUserName(){
        User foundByRepo = userRepository.findByName(user.getName());
        Assertions.assertEquals(result,foundByRepo);
    }
    @Test
    public void updateUser(){
        User oldUser = userRepository.findByName(user.getName());
        oldUser.setName("Aaaaaaa");
        userRepository.save(oldUser);
        User newUser = userRepository.findByName(oldUser.getName());
        Assertions.assertEquals(oldUser.getName(),newUser.getName());
        Assertions.assertEquals(oldUser.getId(),newUser.getId());
    }
    @Test
    public void updateUserWithConstructorWithId() throws Exception{
        User newUser = new User(user.getId(),"aa","Nameasdfafd1_");
        userRepository.save(newUser);
        Assertions.assertEquals(newUser.getName(),userRepository.getById(newUser.getId()).getName());
    }
}
