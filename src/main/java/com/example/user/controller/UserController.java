package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.security.JwtUtil;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
