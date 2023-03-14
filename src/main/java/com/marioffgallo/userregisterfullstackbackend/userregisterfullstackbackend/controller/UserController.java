package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.controller;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.integration.MessageProducer;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.LogEvent;

import java.net.URI;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/database")
public class UserController {

    @Autowired
    UserService userServices;

    @Autowired
    private MessageProducer messageProducer;


    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> list = userServices.getAllUsers();

        LogEvent logEvent = new LogEvent();
        logEvent.setAction("GET");
        logEvent.setDate(new Date());
        logEvent.setPayload("Feito busca por todos usuários no DB");

        messageProducer.send(logEvent);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> findUserById(@PathVariable int id){
        User user = userServices.getUserById(id);

        LogEvent logEvent = new LogEvent();
        logEvent.setAction("GET");
        logEvent.setDate(new Date());
        logEvent.setPayload("Usuário buscado: " + user.toString());

        messageProducer.send(logEvent);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<User> create(@RequestBody User user){
        user.setBirthDate(new java.sql.Date(user.getBirthDate().getTime()));

        user = userServices.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        LogEvent logEvent = new LogEvent();
        logEvent.setAction("POST");
        logEvent.setDate(new Date());
        logEvent.setPayload("Usuário criado: " + user.toString());

        messageProducer.send(logEvent);

        return ResponseEntity.created(uri).body(user);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        User user = userServices.getUserById(id);
        userServices.delete(id);

        LogEvent logEvent = new LogEvent();
        logEvent.setAction("DELETE");
        logEvent.setDate(new Date());
        logEvent.setPayload("Usuário deletado: " + user.toString());

        messageProducer.send(logEvent);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user){
        user = userServices.update(id, user);

        LogEvent logEvent = new LogEvent();
        user.setId(id);
        logEvent.setAction("PUT");
        logEvent.setDate(new Date());
        logEvent.setPayload("Usuário atualizado: " + user.toString());

        messageProducer.send(logEvent);

        return ResponseEntity.ok().body(user);
    }
}
