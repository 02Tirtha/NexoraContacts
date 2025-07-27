package com.nexora.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nexora.entities.User;
import com.nexora.entities.providers;
import com.nexora.helper.AppConstants;
import com.nexora.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    
    private final UserRepo userRepo;

    public OAuthAuthenticationSuccessHandler( UserRepo userRepo)
    {
        this.userRepo=userRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                logger.info("OAuthAuthenticationSuccessHandler");

          DefaultOAuth2User  user= (DefaultOAuth2User)authentication.getPrincipal();
                logger.info(user.getName());
                user.getAttributes().forEach((key,value)->{
                    logger.info("{} => {}", key, value);

                });
                logger.info(user.getAuthorities().toString());

                //  data database save
                String email = user.getAttribute("email").toString();
                String name = user.getAttribute("name").toString();
                String picture = user.getAttribute("picture").toString();

               // Check if user already exists
        User existingUser = userRepo.findByEmail(email).orElse(null);

        if (existingUser == null) {
            // Create and save user
            User newUser = new User();
            newUser.setId(UUID.randomUUID().toString());
            newUser.setEmail(email);
            newUser.setFirstName(name);
            newUser.setProfilePic(picture);
            newUser.setPassword("password"); // dummy password
            newUser.setProvider(providers.GOOGLE);
            newUser.setEnable(true);
            newUser.setEmailVerified(true);
            newUser.setProviderId(user.getName());
            newUser.setRoleList(List.of(AppConstants.ROLE_USER));
            
            userRepo.save(newUser);

            logger.info(" User saved: {}", email);
        } else {
            logger.info(" User already exists: {}", email);
        }
               response.sendRedirect("/user/profile");
         
            }
    
    

}
