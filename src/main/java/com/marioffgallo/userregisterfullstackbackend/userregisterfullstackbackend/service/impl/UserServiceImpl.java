package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.impl;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.LogEventDTO;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.repository.UserRepository;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.UserService;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.DatabaseException;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<User> getAllUsers(boolean invertOrder) {
        if(invertOrder){
            return repository.findAllByOrderByIdDesc();
        } else {
            return repository.findAllByOrderByIdAsc();
        }
    }

    @Override
    public User getUserById(int id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(int id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public User update(int id, User obj) {
        try {
            User entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }

    }

    @Override
    public LogEventDTO createLog(LogEventDTO logEventDTO) {
        ResponseEntity<LogEventDTO> responseEntity = restTemplate
                .postForEntity("http://localhost:9192/api/database/logs/create", logEventDTO, LogEventDTO.class);

        return responseEntity.getBody();
    }

    private void updateData(User user, User userToSave) {
        user.setName(userToSave.getName());
        user.setAge(userToSave.getAge());
        user.setEmail(userToSave.getEmail());
        user.setBirthDate(new java.sql.Date(userToSave.getBirthDate().getTime()));
    }
}

