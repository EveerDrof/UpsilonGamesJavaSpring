package com.diploma.UpsilonGames.users;

import com.diploma.UpsilonGames.IMarkAcceptableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService implements IMarkAcceptableService, UserDetailsService {
    private UserRepository userRepository;
    public HashMap<String, String> userToHashMap(User user){
        HashMap<String,String> result = new HashMap<>();
        result.put("name",user.getName());
        result.put("id",String.valueOf(user.getId()));
        return result;
    }
    @Autowired
    public UserService(@Lazy UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User save(User user){
        return userRepository.save(user);
    }
    public User findByName(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByName(userName);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
    public User findById(long userId){
        return userRepository.getById(userId);
    }
    public boolean existsById(long userId){
        return userRepository.existsById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return findByName(s);
    }
    public boolean existsByName(String name){
        return userRepository.existsByName(name);
    }

}
