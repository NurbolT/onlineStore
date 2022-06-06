package com.example.onlinestore.auth.controller;

import com.example.onlinestore.auth.entity.Role;
import com.example.onlinestore.auth.entity.User;
import com.example.onlinestore.auth.service.UserDetailsImpl;
import com.example.onlinestore.auth.service.UserDetailsServiceImpl;
import com.example.onlinestore.auth.service.UserService;
import com.example.onlinestore.exception.UserOrEmailExistsException;
import com.example.onlinestore.util.CookieUtils;
import com.example.onlinestore.util.JwtUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.onlinestore.auth.service.UserService.DEFAULT_ROLE;

@RestController
@Log
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookieUtils cookieUtils, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
        this.userDetailsService = userDetailsService;
    }




    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) throws RoleNotFoundException{

//        if((userService.getByEmail(user.getEmail()) != null) || (userService.getByUserName(user.getUsername()) != null)){
//            throw new UserOrEmailExistsException("User or email already exists");
//        }

        if(userService.userExists(user.getUsername(), user.getEmail())){
            throw new UserOrEmailExistsException("User or email already exists");
        }

        Role userRole = userService.getByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found."));

        user.getRoles().add(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
        Date date = new Date();
        user.setCreatedAt(date);
        userService.register(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.createAccessToken(userDetails.getUser());
        userDetails.getUser().setPassword(null);

        HttpCookie cookie = cookieUtils.createJwtCookie(jwt);

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity logout(){
        HttpCookie cookie = cookieUtils.deleteJwtCookie();

        HttpHeaders responsHeaders = new HttpHeaders();
        responsHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responsHeaders).build();

    }

}
