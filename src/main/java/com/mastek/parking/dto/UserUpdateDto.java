package com.mastek.parking.dto;

import lombok.Data;

import java.util.Date;
@Data
public class UserUpdateDto {

    private Long id;
    private String password;
    private String mobileNumber;
    private Date updated_date;  //timestamp
    private String status;
}
