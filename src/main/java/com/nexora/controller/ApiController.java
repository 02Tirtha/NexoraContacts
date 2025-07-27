package com.nexora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexora.entities.Contact;
import com.nexora.seriveces.ContactServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired 
    private ContactServices contactServices;

    @GetMapping("/{contactId}")
    public Contact geContact(@PathVariable String contactId) {
        return contactServices.getById(contactId);
    }
    
} 
