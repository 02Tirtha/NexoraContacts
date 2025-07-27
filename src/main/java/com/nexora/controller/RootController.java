package com.nexora.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.nexora.entities.User;
import com.nexora.seriveces.UserServices;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserServices userServices;

    @ModelAttribute
        public void addLoggedUserInfo(Model model , Principal principal)
        {
            System.out.println("Adding User To the model");
            String email = "Unknown";
            String name = "Unknown";
            boolean loggedInUser = false;

    if (principal instanceof org.springframework.security.core.Authentication auth) {

        Object principalObj = auth.getPrincipal();

        //  For Form Login
        if (principalObj instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            email = userDetails.getUsername(); // usually email
            // Optionally fetch name from DB if available
            User user = userServices.getUserByEmail(email);
            name = user.getFirstName() + " " + user.getLastName();
            loggedInUser = true;
        }

        //  For OAuth Login
        if (principalObj instanceof org.springframework.security.oauth2.core.user.OAuth2User oauthUser) {
            Object emailObj = oauthUser.getAttributes().get("email");
            Object nameObj = oauthUser.getAttributes().get("name");
            loggedInUser = true;

            email = emailObj != null ? emailObj.toString() : "No email";
            name = nameObj != null ? nameObj.toString() : "No name";
        }
    }

    model.addAttribute("email", email);
    model.addAttribute("name", name);
    model.addAttribute("loggedInUser", loggedInUser);
    System.out.println("==> Logged in user:");
    System.out.println("Name  : " + name);
    System.out.println("Email : " + email);
System.out.println("LOGGED IN USER IN MODEL: " + model.getAttribute("loggedInUser"));


        }


}

