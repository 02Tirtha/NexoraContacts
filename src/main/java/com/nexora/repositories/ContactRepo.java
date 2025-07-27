package com.nexora.repositories;

import com.nexora.entities.Contact;
import com.nexora.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;


@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{
    // find contact by User
    // custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);
   
    // find contact by UserId
    // custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId")String userid);

   Contact findByIdAndUserId(String id, String userId);

    Page<Contact> findByNameContainingAndUser(String nameKeyword,User user,Pageable pageable);
    Page<Contact> findByEmailContainingAndUser(String emailKeyword,User user,Pageable pageable);
    Page<Contact> findByPhoneNumContainingAndUser(String phoneNum,User user, Pageable pageable);


    
}
