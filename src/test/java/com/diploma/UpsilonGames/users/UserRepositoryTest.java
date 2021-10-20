package com.diploma.UpsilonGames.users;

import org.junit.BeforeClass;
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
    public void setUp(){
        user = new User("Michael");
        result = testEntityManager.persistFlushFind(user);
    }
    @Test
    public void addAndGetUserById(){
        Assertions.assertTrue(user.equals(result));
        User foundByRepo = userRepository.getById(user.getId());
        Assertions.assertEquals(result,foundByRepo);
    }
    @Test
    public void getByUserName(){
        User foundByRepo = userRepository.getByName(user.getName());
        Assertions.assertEquals(result,foundByRepo);
    }
}
