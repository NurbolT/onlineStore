package com.example.onlinestore.auth.service;

import com.example.onlinestore.auth.entity.User;
import com.example.onlinestore.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if(!user.isPresent()){
            user = userRepository.findByEmail(username);
        }

        if(!user.isPresent()){
            throw new UsernameNotFoundException("User Not Found with username or email: " + username);
        }

        return new UserDetailsImpl(user.get());
    }
}
