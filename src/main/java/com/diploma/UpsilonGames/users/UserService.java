package com.diploma.UpsilonGames.users;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User save(User user){
        return userRepository.save(user);
    }

    public HashMap<String,String> findByName(String userName) throws Exception {
        User user = userRepository.findByName(userName);
        if(user == null){
            throw new Exception("User not found");
        }
        HashMap<String,String> result = new HashMap<>();
        result.put("name",user.getName());
        result.put("id",String.valueOf(user.getId()));
        return result;
    }
}
