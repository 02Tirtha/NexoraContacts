package com.nexora.forms;

import org.springframework.web.multipart.MultipartFile;

import com.nexora.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {
    private String name;
    private String email;
    private User User;
    private String phoneNum;
    private String address;
    private boolean favorite;
    private String webLink;
    private String linkedInLink;
    private MultipartFile contactImage;
}
