package com.springbootacademy.security.repository;

import com.springbootacademy.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
//    List<User> findBymllmlmlml(String username); // This is the method initially created from the BankUsernamePasswordAuthenticationProvider class.

    List<User> findByEmail(String username);
}
