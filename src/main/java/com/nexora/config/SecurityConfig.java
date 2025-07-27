package com.nexora.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import com.nexora.helper.CustomAuthorizationRequestResolver;
import com.nexora.seriveces.CustomOAuth2UserService;
import com.nexora.serviceImpl.SecurityCustomerUserDetailService;



@Configuration
public class SecurityConfig {
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private SecurityCustomerUserDetailService userDetailService;

    @Autowired
private ClientRegistrationRepository clientRegistrationRepository;


    // Configuration of authentication provider to spring security
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        // password encoder  object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
   
}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
         OAuth2AuthorizationRequestResolver resolver =
        new CustomAuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        httpSecurity.authorizeHttpRequests(authorize->
        {
            authorize.requestMatchers("/user/**").authenticated();
            // authorize.requestMatchers("/do_register", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
            authorize.anyRequest().permitAll();
        })
            .formLogin(form -> form
        .usernameParameter("email") // Tell Spring to expect 'email' instead of 'username'
        .passwordParameter("password")
        .loginPage("/security-login")
        .loginProcessingUrl("/perform-login")
         .failureUrl("/security-login?error=true") // redirects with error param
        .defaultSuccessUrl("/user/profile", true)
        .permitAll()
    )
    

        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/security-login?logout")
        )
  .oauth2Login(oauth2 -> oauth2
            .loginPage("/security-login")
            .authorizationEndpoint(endpoint -> endpoint
                .authorizationRequestResolver(resolver)
             )
              .userInfoEndpoint(userInfo -> userInfo
        .userService(customOAuth2UserService) // 🔐 BLOCK non-enabled users here
    )
            .successHandler(handler)
            // .defaultSuccessUrl("/user/dashboard", true)
            .failureUrl("/security-login?error=true")
        )

       .csrf(csrf -> csrf.disable()); // ✅ disable CSRF for testing OR customize it


       
        // oAuth config
        // httpSecurity.oauth2Login(Customizer.withDefaults());

       return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    
}


