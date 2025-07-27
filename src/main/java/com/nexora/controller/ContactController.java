package com.nexora.controller;

import com.cloudinary.Cloudinary;
import com.nexora.entities.Contact;
import com.nexora.entities.User;
import com.nexora.forms.ContactForm;
import com.nexora.helper.Helper;
import com.nexora.helper.ResourceNotFoundException;
import com.nexora.repositories.ContactRepo;
import com.nexora.repositories.UserRepo;
import com.nexora.seriveces.ContactServices;
import com.nexora.seriveces.UserServices;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger =  LoggerFactory.getLogger(ContactController.class);;
 
 
    // @Autowired
    // private ImageService imageService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ContactServices contactServices;
    @Autowired
    private UserServices userServices;

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("showUserNavbar", true);
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
public String saveContact(@ModelAttribute("contactForm") ContactForm contactForm, Authentication authentication, RedirectAttributes redirectAttributes) {
    String username = Helper.getEmailOfLoggedInUser(authentication);
    
    User user=userServices.getUserByEmail(username);
   
    // Upload Image code
    // String fileURL= imageService.uploadImage(contactForm.getContactImage());
    // Image Process
    // logger.info("file info: {}", contactForm.getContactImage().getOriginalFilename());
    
//    Contact 
    Contact contact = new Contact();
    
    contact.setName( contactForm.getName());
    contact.setEmail(contactForm.getEmail());
    contact.setAddress(contactForm.getAddress());
    contact.setFavorite(contactForm.isFavorite());
    contact.setLinkedInLink(contactForm.getLinkedInLink());
    contact.setPhoneNum(contactForm.getPhoneNum());
    contact.setWebLink(contactForm.getWebLink());
    // contact.setPicture(fileURL);
    contact.setUser(user); 
    // process the form data
    contactRepo.save(contact);

    
    System.out.println("=== FORM SUBMITTED ===");
    System.out.println("Name     : " + contactForm.getName());
    System.out.println("Email    : " + contactForm.getEmail());
    System.out.println("Phone    : " + contactForm.getPhoneNum());
    System.out.println("Address  : " + contactForm.getAddress());
    System.out.println("LinkedIn : " + contactForm.getLinkedInLink());
    System.out.println("WebLink  : " + contactForm.getWebLink());
    System.out.println("Favorite : " + contactForm.isFavorite());
    System.out.println("======================");

    redirectAttributes.addFlashAttribute("success", "You have successfully added a new contact!");
    return "redirect:/user/contacts/add";
}

// view contacts
@RequestMapping()
public String viewContacts(
    @RequestParam(value="page",defaultValue = "0" ) int page,
    @RequestParam(value="size",defaultValue = "4") int size,
    @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
    @RequestParam(value="direction", defaultValue = "asc") String direction,
    Model model, 
    Authentication authentication
){
    model.addAttribute("showUserNavbar", true);
    
    // Load all the user contacts
   String username= Helper.getEmailOfLoggedInUser(authentication);
    
   User user =userServices.getUserByEmail(username);
   Page<Contact> pageContacts = contactServices.getByUser(user, page ,size, sortBy, direction);

   model.addAttribute("contacts", pageContacts);
   model.addAttribute("currentPage", page);
    return "user/contacts";
}

    // serach handler
 @RequestMapping("/search")
public String serchHeandler(
    @RequestParam(value="field",required = false) String field,
    @RequestParam(value="query",required = false) String value,
    @RequestParam(value = "size", defaultValue = "4") int size,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
    @RequestParam(value = "direction", defaultValue = "asc") String direction,
    Model model,
    Authentication authentication
) {
     model.addAttribute("showUserNavbar", true);
    if (size < 1) size = 4;

    var user = userServices.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
    Page<Contact> pageContact = null;

    // Initial search
    if (value == null || value.trim().isEmpty()  || (field == null || field.trim().isEmpty()) ) {
    // Show all contacts if query is empty
 return "redirect:/user/contacts";
} else{
    if (field.equalsIgnoreCase("name")) {
        pageContact = contactServices.searchByName(value, page, size, sortBy, direction, user);
    } else if (field.equalsIgnoreCase("email")) {
        pageContact = contactServices.searchByEmail(value, page, size, sortBy, direction, user);
    } else if (field.equalsIgnoreCase("phone") || field.equalsIgnoreCase("phoneNum")) {
        pageContact = contactServices.searchByPhone(value, page, size, sortBy, direction, user);
    }
}
    //  Fix: Prevent requesting page > totalPages
    if (pageContact != null && page >= pageContact.getTotalPages() && pageContact.getTotalPages() > 0) {
        page = pageContact.getTotalPages() - 1;

        // Re-fetch with corrected page
        if (field.equalsIgnoreCase("name")) {
            pageContact = contactServices.searchByName(value, page, size, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("email")) {
            pageContact = contactServices.searchByEmail(value, page, size, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("phoneNum")) {
            pageContact = contactServices.searchByPhone(value, page, size, sortBy, direction, user);
        }
    }
    if (pageContact == null || pageContact.isEmpty()) {
    model.addAttribute("message", "No results found");
    
}

    // logger.info("Corrected Page: {} of {}", page, pageContact.getTotalPages());
    model.addAttribute("pageContact", pageContact);
    model.addAttribute("query", value);
    model.addAttribute("field", field);
    model.addAttribute("currentPage", page);

    return "user/search";
}

// Profile
//         @RequestMapping(value="/view/{id}", method=RequestMethod.GET)
//        public String userProfile(Principal principal, Model model) {
//             logger.info("Principal Class: " + principal.getClass().getName());
//             model.addAttribute("showUserNavbar", true);
//             return "user/view";
// }

// view

@GetMapping("/view/{id}")
public String userProfile(@PathVariable String id, Principal principal, Model model) {
    //  Extract email from OAuth2
    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
    OAuth2User oauthUser = oauthToken.getPrincipal();
    String email = oauthUser.getAttribute("email");

    logger.info(" Email from OAuth2 Principal: {}", email);

    //  Get user
    User user = userServices.getUserByEmail(email);
    if (user == null) {
        throw new ResourceNotFoundException("User not found");
    }

    //  Fetch contact by ID
    Contact contact = contactServices.getById(id);
    if (contact == null) {
        throw new ResourceNotFoundException("Contact not found");
    }

    //  Check if the contact belongs to the current user
    if (!contact.getUser().getId().equals(user.getId())) {
        logger.warn("Access denied: Contact does not belong to logged-in user");
        throw new ResourceNotFoundException("Contact not found or access denied");
    }

    //  All good, pass data to view
    model.addAttribute("user", user);
    model.addAttribute("contact", contact);
    model.addAttribute("showUserNavbar", true);
    return "user/view";
}

    // Delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteCotnact(
        @PathVariable String contactId    )
    {
        contactServices.delete(contactId);
        System.out.println("Delted Cotnact " + contactId);
        logger.info("contactID {} deleted",contactId);
        return "redirect:/user/contacts";
    }
    
    // update 
    @GetMapping("/update/{id}")
public String updateUser(@PathVariable String id, Principal principal, Model model) {
   String email;
    // Handle both OAuth2 and standard login
    if (principal instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken oauthToken) {
        email = oauthToken.getPrincipal().getAttribute("email");
    } else {
        email = principal.getName();  // works for form login
    }

    logger.info(" Email from Principal: {}", email);
    //  Extract email from OAuth2
    // OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
    // OAuth2User oauthUser = oauthToken.getPrincipal();
    // String email = oauthUser.getAttribute("email");

    logger.info(" Email from OAuth2 Principal: {}", email);

    //  Get user
    User user = userServices.getUserByEmail(email);
    
    //  Fetch contact by ID
    Contact contact = contactServices.getById(id);

    //  All good, pass data to view
    model.addAttribute("user", user);
    model.addAttribute("contact", contact);
    model.addAttribute("showUserNavbar", true);
    return "user/edit_contact";
}

//    contact updated 
@PostMapping("/edit")
public String processUpdate(@ModelAttribute Contact contact, Principal principal, RedirectAttributes redirectAttributes) {
     String email;

    // Supports both OAuth2 and form login
    if (principal instanceof OAuth2AuthenticationToken oauthToken) {
        email = oauthToken.getPrincipal().getAttribute("email");
    } else {
        email = principal.getName();
    }

    User user = userServices.getUserByEmail(email);
     // Re-fetch contact and check owner
    Contact existing = contactServices.getById(contact.getId());
    if (!existing.getUser().getId().equals(user.getId())) {
        throw new AccessDeniedException("You cannot update this contact.");
    }

    contact.setUser(user); // Ensure correct user
    contactServices.save(contact);

    // // Get logged-in user from OAuth2
    // OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
    // String email = oauthToken.getPrincipal().getAttribute("email");
    // User user = userServices.getUserByEmail(email);

    // // Check ownership again
    // Contact existing = contactServices.getById(contact.getId());
    // if (!existing.getUser().getId().equals(user.getId())) {
    //     throw new AccessDeniedException("You cannot update this contact.");
    // }

    // contact.setUser(user); // Re-link the user to prevent detach
    // contactServices.save(contact);

    redirectAttributes.addFlashAttribute("message", "Contact updated successfully!");
    return "redirect:/user/contacts";
}
 // --- Show Profile Edit Form (fix route and no @PathVariable!) ---
    @GetMapping("/update-profile")
public String showUserProfileEditForm(Principal principal, Model model) {
    String email;

    if (principal instanceof OAuth2AuthenticationToken oauthToken) {
        email = oauthToken.getPrincipal().getAttribute("email");
    } else {
        email = principal.getName();
    }

    Optional<User> userOptional = userRepo.findByEmail(email);

    if (userOptional.isPresent()) {
        User user = userOptional.get();
        model.addAttribute("user", user);
        return "user/edit_profile";
    }

    return "redirect:/security-login";
}


    // --- Handle Profile Update ---
    

    @PostMapping("/edit-profile")
public String processUpdateProfile(@ModelAttribute("user") User updatedUser,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
    String email = (principal instanceof OAuth2AuthenticationToken oauth)
            ? oauth.getPrincipal().getAttribute("email")
            : principal.getName();

    User user = userServices.getUserByEmail(email);

    user.setPhoneNum(updatedUser.getPhoneNum());
    user.setFirstName(updatedUser.getFirstName());
    user.setLastName(updatedUser.getLastName());

    userRepo.save(user);

    redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
    return "redirect:/user/profile";
}

  @GetMapping("/settings")
    public String settingsPage() {
        return "user/settings";  // looks for templates/user/settings.html
    }

}