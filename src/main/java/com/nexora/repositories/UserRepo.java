package com.nexora.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexora.entities.User;


public interface UserRepo extends JpaRepository<User, String>{
    Optional<User> deleteById(User user2);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String userId);
User getUserByEmail(String email); // ✅ Spring can now generate query
}
