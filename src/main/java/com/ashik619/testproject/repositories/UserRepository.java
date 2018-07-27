package com.ashik619.testproject.repositories;

import com.ashik619.testproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = "SELECT * FROM users  WHERE user_name  = ?1",
            nativeQuery = true)
    List<User> findUserByName(String username);

    @Query(value = "SELECT * FROM users  WHERE user_name  = ?1 AND password = ?2",
            nativeQuery = true)
    List<User> findUserByNameAndPass(String username,String password);

}