package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.repository.UserRepository;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.DatabaseException;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User create(User user) {
        return repository.save(user);
    }

    public void delete(int id) {
        try{
            repository.deleteById(id);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch(DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update(int id, User obj) {
        try {
            User entity = repository.getOne(id);
            updateData(entity, obj);
            return repository.save(entity);
        }
        catch(EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }

    }

    private void updateData(User user, User userToSave) {
        user.setName(userToSave.getName());
        user.setAge(userToSave.getAge());
        user.setEmail(userToSave.getEmail());
        user.setBirthDate(new java.sql.Date(userToSave.getBirthDate().getTime()));
    }
}
