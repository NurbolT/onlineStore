package com.example.onlinestore.auth.service;

import com.example.onlinestore.auth.entity.Role;
import com.example.onlinestore.auth.entity.User;
import com.example.onlinestore.auth.repository.RoleRepository;
import com.example.onlinestore.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final String DEFAULT_ROLE = "USER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void register(User user){
        userRepository.save(user);
    }

    public boolean userExists(String username, String email){
        if(userRepository.existsByEmail(email) || userRepository.existsByUsername(username)){
            return true;
        }
        return false;
    }

    public Optional<User> getByUserName(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<Role> getByName(String name){
        return roleRepository.findByName(name);
    }


}
