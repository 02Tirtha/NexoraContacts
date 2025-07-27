package com.nexora.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nexora.entities.Contact;
import com.nexora.entities.User;
import com.nexora.helper.ResourceNotFoundException;
import com.nexora.repositories.ContactRepo;
import com.nexora.repositories.UserRepo;
import com.nexora.seriveces.ContactServices;
@Service
public class ContactServiceImpl implements ContactServices {

    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;
    
    @Override
    public Contact save(Contact contact) {
       return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();     
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with given id"+ id) );    
    }

    @Override
    public void delete(String id) {
        contactRepo.deleteById(id);
    }


    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page , int size, String sortBy , String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
}

    @Override
    public Page<Contact> searchByName(String nameKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
                if (size < 1) size = 4;
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending(): Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort); 
        return contactRepo.findByNameContainingAndUser(nameKeyword,user,  pageable);
    }            

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortField,
            String sortDirection , User user) {
                if (size < 1) size = 4;
       Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending(): Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort); 
        return contactRepo.findByEmailContainingAndUser(emailKeyword,user, pageable);
    }

   @Override
public Page<Contact> searchByPhone(String phoneKeyword, int page, int size, String sortField, String sortDirection , User user) {
    Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending(): Sort.by(sortField).ascending();
    Pageable pageable = PageRequest.of(page, size, sort); 
    Page<Contact> results = contactRepo.findByPhoneNumContainingAndUser(phoneKeyword,user, pageable);

    System.out.println("== Phone search: " + phoneKeyword);
    results.forEach(c -> {
        System.out.println("Contact: " + c.getName() + ", Phone: " + c.getPhoneNum());
    });

    return results;
}


    
}
