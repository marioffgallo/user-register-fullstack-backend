package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.repository;

import com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findAllByOrderByIdAsc();
    public List<User> findAllByOrderByIdDesc();
}
