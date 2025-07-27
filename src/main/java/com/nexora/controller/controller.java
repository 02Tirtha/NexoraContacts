package com.nexora.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import com.nexora.entities.User;
import com.nexora.forms.UserForm;

import com.nexora.helper.Message;
import com.nexora.helper.MessageType;
import com.nexora.seriveces.UserServices;
import com.nexora.repositories.UserRepo;
import org.springframework.web.bind.annotation.RequestParam;




    @Controller
    public class controller extends RootController{
        private Logger logger = LoggerFactory.getLogger(controller.class);

        @Autowired
        private UserRepo userRepo;
        @Autowired
        private UserServices UserServices;

          @Autowired
        private PasswordEncoder passwordEncoder;

        @RequestMapping("/home")
        public String home(Model model)
        {
            model.addAttribute("name", "Tirtha");
            model.addAttribute("link","xyz");
            return "home";
        }

        @RequestMapping("/about")
        public String about()
        {
            System.out.println("about page");
            return "about";
        }
        @RequestMapping("/services")
        public String services()
        {
            System.out.println("Services page");
            return "services";
        }

    @RequestMapping("/contact")
        public String contact() {
            System.out.println("Contact Us");
            return "contact"; 
        }

        @RequestMapping(value="/exploreFeature",method=RequestMethod.GET)
        public String exploreFeature() {
            return "exploreFeature";
        }
        

        @RequestMapping("/login")
        public String login()
        {
            System.out.println("Login/Sign Up");
            return "login";
        }

        @GetMapping("/signup")
        public String signUp(Model model)
        {   
            UserForm userForm = new UserForm(); // User form is use for cacth the data from signup form and send it to controller convert  it into a user object. 
            model.addAttribute("userForm", userForm);
            return "signup";
        }

       @RequestMapping(value="/do_register", method = RequestMethod.POST)
        public String register(@ModelAttribute UserForm userForm, RedirectAttributes redirectAttrs) {
        try {
                
        System.out.println("Received form: " + userForm);

        User user = new User();
        user.setFirstName(userForm.getFirst_Name());
        user.setLastName(userForm.getLast_Name());
        user.setEmail(userForm.getEmail());
        user.setPhoneNum(userForm.getPhone_num());

        // If you're using Spring Security — encode the password
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));

        // Optional: confirm password should not go to DB
        user.setConfirmPassword(userForm.getConfirm_Password());  // <-- REMOVE or mark @Transient

        UserServices.save(user);  // this is likely where error is

        Message msg = new Message("Registration successful!", MessageType.green);
        redirectAttrs.addFlashAttribute("flashMessage", msg);

        
        return "redirect:/signup";

    } catch (Exception e) {
        System.out.println("ERROR during registration:");
        e.printStackTrace(); // <--- this will show the real reason in your console
        return "error"; // make sure you have an error.html, or create a dummy for now
    }
}

// Dashboard
       @RequestMapping(value="user/dashboard", method=RequestMethod.GET)
public String userDashboard(@AuthenticationPrincipal OAuth2User oauthUser, Model model) {
    model.addAttribute("showUserNavbar", true);

    if (oauthUser != null) {
        String name = oauthUser.getAttribute("name");
        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");

        model.addAttribute("name", name);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);
    }

    System.out.println("Dashboard");
    return "user/dashboard";
}

        
// Profile
//         @RequestMapping(value="user/profile", method=RequestMethod.GET)
//        public String userProfile(Principal principal, Model model) {
//             logger.info("Principal Class: " + principal.getClass().getName());
//             model.addAttribute("showUserNavbar", true);
//             return "user/profile";
// }


@RequestMapping(value = "user/profile", method = RequestMethod.GET)
public String userProfile(@AuthenticationPrincipal OAuth2User oauthUser, Model model) {
    model.addAttribute("showUserNavbar", true);

    if (oauthUser != null) {
        
        String email = oauthUser.getAttribute("email");  // Get email from OAuth provider

        // fetch User from DB
        User user = userRepo.findByEmail(email)
                .orElseGet(() -> {
                    // Optionally create a User if not exists
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstName(oauthUser.getAttribute("given_name"));
                    newUser.setLastName(oauthUser.getAttribute("family_name"));
                    return userRepo.save(newUser);
                });
                System.out.println("Fetched user: " + user);
System.out.println("Phone number: " + user.getPhoneNum());

        // put user data in the model
        model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNum", user.getPhoneNum());
        model.addAttribute("user", user);
    }

    return "user/profile";
}

// Security Login
        @RequestMapping("/security-login")
        public String secureLoginPage() {
            return "security-login"; // This will return security-login.html from templates
        }
        

    }
