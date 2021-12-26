package com.diploma.UpsilonGames.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping(value = "users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(@Lazy UserService userService){
        this.userService = userService;
    }
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody HashMap<String,String> hashMap){
        HttpStatus status = HttpStatus.CREATED;
        String errorMessage = "Created successfully";
        try {
            User user = new User(hashMap.get("name"), hashMap.get("password"));
            userService.save(user);
        }catch (IncorrectPasswordException exception) {
            errorMessage = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity response = new ResponseEntity(errorMessage,status);
        return response;
    }
    @GetMapping("/loggedUserData")
    public  ResponseEntity getUser( Principal principal){
        User user;
        try{
            user = userService.findByName(principal.getName());
        } catch (Exception ex){
            return new ResponseEntity("errorMessage",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user,HttpStatus.OK);
    }
}
