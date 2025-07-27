package com.nexora.seriveces;

import java.util.List;

import org.springframework.data.domain.Page;
import com.nexora.entities.Contact;
import com.nexora.entities.User;

public interface ContactServices {
    Contact save(Contact contact);
    Contact update(Contact contact);
    List<Contact> getAll();
    Contact getById(String id);
    void delete (String id);
    Page<Contact> searchByName(String nameKeywordint,int page , int size,String sortField, String sortDirection, User user);
    Page<Contact> searchByEmail(String emailKeywordint,int page , int size,String sortField, String sortDirection, User user);
    Page<Contact> searchByPhone(String phoneKeywordint,int page , int size,String sortField, String sortDirection, User user);
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user,int page , int size,String sortField, String sortDirection);
}