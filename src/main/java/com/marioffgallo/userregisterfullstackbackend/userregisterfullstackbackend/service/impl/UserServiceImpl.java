package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.impl;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.LogEventDTO;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.repository.UserRepository;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.UserService;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.DatabaseException;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Service for interactions with the database MySQL
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${backend-log.url}")
    String backendLogUrl;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves all users in the repository
     *
     * @param invertOrder boolean specifying the order to retrieve the list
     * @return A list of all users
     * @throws DatabaseException if an error occurs when retrieving the list
     */
    @Override
    public List<User> getAllUsers(boolean invertOrder) throws DatabaseException {
        if (invertOrder) {
            try {
                return repository.findAllByOrderByIdDesc();
            } catch (Exception e) {
                throw new DatabaseException(e.getMessage());
            }
        } else {
            try {
                return repository.findAllByOrderByIdAsc();
            } catch (Exception e) {
                throw new DatabaseException(e.getMessage());
            }
        }
    }

    /**
     * Receives ID as a parameter and returns the user if found it
     *
     * @param id integer specifying the number ID
     * @return Returns a user if found in the DB
     * @throws ResourceNotFoundException if nothing is found
     */
    @Override
    public User getUserById(int id) throws ResourceNotFoundException {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Receives User and saves in the database
     *
     * @param user a UserDB object
     * @return Returns the user with the ID generated in the database
     * @throws DatabaseException if an error occurs when saving the object
     */
    @Override
    public User create(User user) throws DatabaseException {
        try {
            return repository.save(user);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Receives ID as a parameter and deletes on database
     *
     * @param id integer specifying the number ID
     * @throws ResourceNotFoundException if nothing is found
     * @throws DatabaseException if an error occurs when saving the object
     */
    @Override
    public void delete(int id) throws ResourceNotFoundException, DatabaseException{
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Receives ID and a User as a parameter, search the id in the database,
     * if exists the user object is updated and saved on the database
     *
     * @param id integer specifying the number ID
     * @param user User object
     * @return Returns the user updated
     * @throws ResourceNotFoundException if nothing is found
     * @throws DatabaseException if an error occurs when saving the object
     */
    @Override
    public User update(int id, User user) throws ResourceNotFoundException, DatabaseException {
        try {
            User entity = repository.getReferenceById(id);
            updateData(entity, user);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Receives LogEventDTO and sends to the microservice user-register-fullstack-backend-log via REST API
     *
     * @param logEventDTO a LogEventDTO object
     * @return Returns LogEventDTO saved in the microservice
     */
    @Override
    public LogEventDTO createLog(LogEventDTO logEventDTO) {
        ResponseEntity<LogEventDTO> responseEntity = restTemplate
                .postForEntity( backendLogUrl + "logs/create", logEventDTO, LogEventDTO.class);

        return responseEntity.getBody();
    }

    /**
     * Receives User from repository and a User to Update
     * updates each field and returns the user from repository
     *
     * @param user User object
     * @param userToSave User object
     * @return Returns user object updated
     */
    private void updateData(User user, User userToSave) {
        user.setName(userToSave.getName());
        user.setAge(userToSave.getAge());
        user.setEmail(userToSave.getEmail());
        user.setBirthDate(new java.sql.Date(userToSave.getBirthDate().getTime()));
    }
}

