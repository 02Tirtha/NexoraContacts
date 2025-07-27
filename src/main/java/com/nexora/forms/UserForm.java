package com.nexora.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
@ToString
public class UserForm {
    private String First_Name;
    private String Last_Name;
    private String Email;
    private String phone_num;
    private String Password;
    private String Confirm_Password;
    
}
