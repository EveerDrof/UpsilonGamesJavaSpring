package com.diploma.UpsilonGames.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/register")
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
    @GetMapping("/{userName}")
    public  ResponseEntity getUser(@PathVariable String userName){
        HashMap userData = new HashMap();
        try{
            userData = userService.findByName(userName);
        } catch (Exception ex){
            userData.put("errorMessage",ex.getMessage());
            return new ResponseEntity(userData,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(userData,HttpStatus.OK);
    }
}
