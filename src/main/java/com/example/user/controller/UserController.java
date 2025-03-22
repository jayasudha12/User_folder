package com.example.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.user.entity.User;
import com.example.user.security.JwtUtil;
import com.example.user.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/token")
    public String generateToken(@RequestBody User user) {
        return jwtUtil.generateToken(user.getEmail());
    }

    @GetMapping("/details")
    public List<User> getUsers(@RequestHeader("Authorization") String token) {
        validateToken(token);
        return userService.getUsers();
    }
    @GetMapping("/getdetails")
    public List<User> getdetails() {
       
        return userService.getdetails();
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user, @RequestHeader("Authorization") String token) {
        validateToken(token);
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, @RequestHeader("Authorization") String token) {
        validateToken(token);
        return userService.deleteUser(id);
    }

    private void validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token format");
        }

        String jwtToken = token.substring(7);
        String email = jwtUtil.extractEmail(jwtToken);

        if (email == null || !jwtUtil.validateToken(jwtToken, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token validation failed");
        }
    }
}
