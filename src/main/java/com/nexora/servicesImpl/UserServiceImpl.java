package com.nexora.serviceImpl;

import java.beans.Transient;
import java.lang.System.LoggerFinder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexora.entities.User;
import com.nexora.helper.AppConstants;
import com.nexora.helper.ResourceNotFoundException;
import com.nexora.repositories.UserRepo;
import com.nexora.seriveces.UserServices;


@Service
public class UserServiceImpl implements UserServices{

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo UserRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public User save(User user) {
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setRoleList(List.of(AppConstants.ROLE_USER));
       return UserRepo.save(user);
    
    }


    @Override
    public Optional<User> getUserById(String userId) {
        return UserRepo.findById(userId);  
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2= UserRepo.findById(user.getId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setPhoneNum(user.getPhoneNum());
        user2.setConfirmPassword(user.getConfirmPassword());
        user2.setProfile(user.getProfile());
        user2.setEnable(user.isEnable());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderId(user.getProviderId());

        User save = UserRepo.save(user2);
        return Optional.ofNullable(save);
    }

   @Override
public Optional<User> deleteUserById(String userId) {
    Optional<User> userOpt = UserRepo.findById(userId);
    userOpt.ifPresent(user -> UserRepo.deleteById(user.getId()));
    return userOpt;
}


    @Override
    public boolean isUserExist(String Userid) {
        User user2= UserRepo.findById(Userid).orElse(null);
        return user2 !=null ? true :false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user2 = UserRepo.findByEmail(email).orElse(null);
        return user2 !=null ? true :false;    
    }


    @Override

        public List<User> getAllUser() {
    return UserRepo.findAll();   
    
    }


    @Override
    public User getUserByEmail(String email) {
        return UserRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not Foud")) ;  
    }
    
}
