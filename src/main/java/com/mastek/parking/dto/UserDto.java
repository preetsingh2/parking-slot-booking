package com.mastek.parking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
    private Date created_date;  //timestamp
    private Date updated_date;  //timestamp
    private String created_by;

    public void getUsername(){
        String firstName = getFirstName() ;
        String lastName = getLastName() ;
        this.created_by = firstName+ " " +lastName;
    }
}
