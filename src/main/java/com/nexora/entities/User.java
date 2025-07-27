package com.nexora.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User implements UserDetails{

    @Id
    @Column(name="userId")
    private String id;

    @Column(name="First_Name", nullable = false)
    private String FirstName;

    @Column(name="Last_Name", nullable = true)
    private String LastName;

    @Column(nullable = false)
    private String email;

    // @Column(nullable = false)
    private String password;

    @Transient
    private String ConfirmPassword;

    @Column(length = 1000)
    private String profile;

    private String phoneNum;

    private String profilePic;

    // Verifications
    private boolean enable=true;
    private boolean emailVerified=false;
    private boolean phoneVerified = false;

    @Enumerated(value = EnumType.STRING)
    //  SELF GOOGLE GITHUB
    private providers provider = providers.SELF;
    private String providerId;

    // Mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =  FetchType.LAZY , orphanRemoval = true)
    private List<Contact> contact = new ArrayList<>();

    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // converting list of roles to granted authorities
        
       Collection<SimpleGrantedAuthority> roles= roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles ;
    }

    
    @Override
    public String getUsername() {
       return this.email;  
    } 
    
    @Transient
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return this.enable;
    }
    public String getFullName() {
    return this.FirstName + " " + this.LastName;
}
}
