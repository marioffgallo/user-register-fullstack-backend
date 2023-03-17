package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model.LogEventDTO;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.repository.UserRepository;
import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllUsers() {
        List<User> expectedEntities = Arrays.asList(
                new User(1, "Teste", 10, "teste@gmail.com", new Date(2020,01,01)),
                new User(2, "Teste", 20, "teste2@gmail.com", new Date(2021,01,01))
        );              ;

        when(userRepository.findAllByOrderByIdAsc()).thenReturn(expectedEntities);

        List<User> actualEntities = userServiceImpl.getAllUsers(false);

        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindUserById() {
        Optional<User> expectedUser = Optional.of(new User(
                1,
                "Teste",
                10,
                "teste@gmail.com",
                new Date(2020,01,01)
        ));

        when(userRepository.findById(1)).thenReturn(expectedUser);

        Optional<User> actualUser = Optional.of(userServiceImpl.getUserById(1));

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testCreateUser() {
        User expectedUser = new User(
                1,
                "Teste",
                10,
                "teste@gmail.com",
                new Date(2020,01,01)
        );

        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        User responseUser = userServiceImpl.create(expectedUser);

        assertEquals(expectedUser, responseUser);
    }

    @Test
    public void testDeleteUser() {
        userServiceImpl.delete(1);

        verify(userRepository).deleteById((1));
    }

    @Test
    public void testUpdateUser() {
        User expectedUser = new User(
                1,
                "Teste2",
                10,
                "teste@gmail.com",
                new Date(2020,01,01)
        );

        User mockUser = new User(
                1,
                "Teste",
                10,
                "teste@gmail.com",
                new Date(2020,01,01)
        );

        when(userRepository.getReferenceById(1)).thenReturn(mockUser);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        User responseUser = userServiceImpl.create(expectedUser);

        assertEquals(expectedUser, responseUser);
    }


    @Test
    public void testCreateLog() {
        String expectedUrl = "http://localhost:9192/api/database/logs/create";

        LogEventDTO expectedLog = new LogEventDTO(
            1,"GET",new Date(2020,01,01),"teste"
        );

        Mockito
                .when(restTemplate.postForEntity(
                        expectedUrl, expectedLog, LogEventDTO.class))
          .thenReturn(new ResponseEntity(expectedLog, HttpStatus.OK));

        LogEventDTO responseLog = userServiceImpl.createLog(expectedLog);

        assertEquals(expectedLog, responseLog);
    }

}

