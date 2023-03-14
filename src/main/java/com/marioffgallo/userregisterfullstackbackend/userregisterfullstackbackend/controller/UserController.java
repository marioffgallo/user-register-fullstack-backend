package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.controller;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/database")
public class UserController {

    @Autowired
    UserService userServices;

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> list = userServices.getAllUsers();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> findUserById(@PathVariable int id){
        User obj = userServices.getUserById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<User> create(@RequestBody User user){
        user.setBirthDate(new java.sql.Date(user.getBirthDate().getTime()));

        user = userServices.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        userServices.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User obj){
        obj = userServices.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
