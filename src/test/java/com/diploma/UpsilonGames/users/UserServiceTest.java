package com.diploma.UpsilonGames.users;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
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
    @Test
    public void getUserByCorrectId_shouldReturnUser() throws Exception{
        User user = new User(1111,"adf","aaaBBBcc111__");
        given(userRepository.getById(user.getId())).willReturn(user);
        User result = userService.findById(user.getId());
        Assertions.assertNotNull(result);
        Assertions.assertTrue(user.equals(result));
    }
    @Test
    public void getUserByIncorrectId_shouldReturnUser() throws Exception{
        User user = new User(1111,"adf","aaaBBBcc111__");
        given(userRepository.getById(user.getId())).willReturn(null);
        User result = userService.findById(user.getId());
        Assertions.assertEquals(null,result);
    }
    @Test void existsById_withNonExistentUser_shouldReturnFalse(){
        given(userRepository.existsById(9999L)).willReturn(false);
        Assertions.assertFalse(userService.existsById(9999L));
    }
    @Test void existsById_withExistentUser_shouldReturnTrue(){
        given(userRepository.existsById(9999L)).willReturn(true);
        Assertions.assertTrue(userService.existsById(9999L));
    }
}
