package com.example.alfa.repositories;

import com.example.alfa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where username = ? and password = ? ", nativeQuery = true)
    User isUserExists(String username, String password);



}
