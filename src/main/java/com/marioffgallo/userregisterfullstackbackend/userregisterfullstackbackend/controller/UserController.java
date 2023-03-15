package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.controller;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.integration.MessageProducer;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.UserDTO;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.LogEventDTO;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/database")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UserService userServices;

    @Autowired
    private MessageProducer messageProducer;


    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> findAllUsers(){
        List<UserDTO> listDTO = userServices.getAllUsers(false)
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        LogEventDTO logEventDTO = new LogEventDTO();
        logEventDTO.setAction("GET");
        logEventDTO.setDate(new Date());
        logEventDTO.setPayload("Feito busca por todos usuários no DB");

        messageProducer.send(logEventDTO);

        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> findAllUsersOrder(@RequestBody boolean invertOrder){
        List<UserDTO> listDTO = userServices.getAllUsers(invertOrder)
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        LogEventDTO logEventDTO = new LogEventDTO();
        logEventDTO.setAction("POST");
        logEventDTO.setDate(new Date());
        logEventDTO.setPayload("Feito busca ordenada por todos usuários no DB");

        userServices.createLog(logEventDTO);

        return ResponseEntity.ok().body(listDTO);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable int id){
        User user = userServices.getUserById(id);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        LogEventDTO logEventDTO = new LogEventDTO();
        logEventDTO.setAction("GET");
        logEventDTO.setDate(new Date());
        logEventDTO.setPayload("Usuário buscado - " + user.toString());

        messageProducer.send(logEventDTO);

        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);

        user.setBirthDate(new java.sql.Date(user.getBirthDate().getTime()));

        user = userServices.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        LogEventDTO logEventDTO = new LogEventDTO();
        logEventDTO.setAction("POST");
        logEventDTO.setDate(new Date());
        logEventDTO.setPayload("Usuário criado - " + user.toString());

        messageProducer.send(logEventDTO);

        UserDTO createdUserDTO = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.created(uri).body(createdUserDTO);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        User user = userServices.getUserById(id);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        userServices.delete(id);

        LogEventDTO logEventDTO = new LogEventDTO();
        logEventDTO.setAction("DELETE");
        logEventDTO.setDate(new Date());
        logEventDTO.setPayload("Usuário deletado - " + userDTO.toString());

        messageProducer.send(logEventDTO);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable int id, @RequestBody UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        user = userServices.update(id, user);

        UserDTO updatedUserDTO = modelMapper.map(user, UserDTO.class);

        LogEventDTO logEventDTO = new LogEventDTO();
        user.setId(id);
        logEventDTO.setAction("PUT");
        logEventDTO.setDate(new Date());
        logEventDTO.setPayload("Usuário atualizado - " + updatedUserDTO.toString());

        messageProducer.send(logEventDTO);

        return ResponseEntity.ok().body(updatedUserDTO);
    }
}
