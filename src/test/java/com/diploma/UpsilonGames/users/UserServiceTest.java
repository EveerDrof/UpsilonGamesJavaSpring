package com.diploma.UpsilonGames.users;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @BeforeEach
    public void setUp() throws Exception {
        this.userService = new UserService(userRepository);
    }
    @Test
    public void saveUser_shouldReturnUser() throws Exception{
        User user =new User("adfasdf","!0aAasdasdadsad");
        given(userRepository.save(any())).willReturn(user);
        User newUser = userService.save(user);
        Assertions.assertTrue(Objects.equals(user,newUser));
    }
    @Test
    public void errorWithTooShortPassword_shouldThrowAnError(){
        Assertions.assertThrows(IncorrectPasswordException.class,()->{
        User user =new User("adfasdf","!0aAa");
        });
    }
    @Test
    public void errorWithPasswordWithoutNumbers_shouldThrowAnError() {
        Assertions.assertThrows(IncorrectPasswordException.class,()->{
        User user =new User("adfasdf","asfdsaadfadfasdfasfA");
        });
    }
}
