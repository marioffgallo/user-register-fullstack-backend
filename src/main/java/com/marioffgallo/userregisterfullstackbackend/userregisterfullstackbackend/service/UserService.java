package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.LogEventDTO;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.repository.UserRepository;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.DatabaseException;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getAllUsers(boolean invertOrder);

    public User getUserById(int id);
    public User create(User user);

    public void delete(int id);

    public User update(int id, User obj);

    public LogEventDTO createLog(LogEventDTO logEventDTO);

}
