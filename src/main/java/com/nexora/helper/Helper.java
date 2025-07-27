package com.nexora.helper;

import java.security.Principal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;


public class Helper {
   
        public static String getEmailOfLoggedInUser(Principal principal)
        {
            if(principal instanceof OAuth2AuthenticationToken oauthToken)
            {
                 // OAuth2 user (Google login)
            Object email = oauthToken.getPrincipal().getAttributes().get("email");

            if (email != null) {
                return email.toString();
                }
                else
                {
                    return "No email found";
                }
            }   
            else
            {
            
                return principal.getName();

            }
            
        }
    
}
