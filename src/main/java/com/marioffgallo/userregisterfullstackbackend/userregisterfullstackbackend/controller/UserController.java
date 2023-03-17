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

/**
 * Controller for the REST calls for users in the application
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
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

    /**
     * Retrieves all users in the repository
     * and log the action on ActiveMQ queue
     *
     * @return A list of all users with an HTTP 200
     */
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

    /**
     * Receives a parameter in the body which flags if is to invert the order which the users are returned
     * and send the log to the microservice user-register-fullstack-backend via REST API
     *
     * @param invertOrder A boolean which informs if is to invert the order
     * @return A list of all users with the order inverted or not with an HTTP 200
     */
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

    /**
     * Receives ID as a parameter in the path to retrieve in the database, log the action on ActiveMQ queue
     * and returns the user
     *
     * @param id an integer specifying the number ID of user
     * @return Returns a user if found in the DB with an HTTP 200
     */
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

    /**
     * Receives UserDTO in the body of request, saves in the database, log the action on ActiveMQ queue
     * and returns the object UserDTO with the ID generated
     *
     * @param userDTO a UserDTO object
     * @return Returns the userDTO with the ID generated in the database with HTTP 201
     */
    @PostMapping(value = "/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
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

    /**
     * Receives ID as a parameter in the path to delete in the database,log the action on ActiveMQ queue
     * and returns only a http 200 with no content
     *
     * @param id an integer specifying the number ID of user
     * @return Returns only a http 204 with no content if success
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
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

    /**
     * Receives an ID as parameter in the path and a UserDTO in the body of request,
     * updates the user in the database, log the action on ActiveMQ queue
     * and returns the object UserDTO updated
     *
     * @param id an integer specifying the number ID of user
     * @param userDTO a UserDTO object
     * @return Returns the userDTO updated and a HTTP 200
     */
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO){
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
