package com.nexora.seriveces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nexora.entities.User;

public interface UserServices {

    User save(User user);
    Optional<User> getUserById(String Id);
    public User getUserByEmail(String email);
    Optional<User> updateUser(User user);
    Optional<User> deleteUserById(String Id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUser();
    
}