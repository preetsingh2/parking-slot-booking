package com.mastek.parking.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "\"user_details\"") // Specify the exact table name with quotes
@Data

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "last name is mandatory")
    @Size(max = 20, message = "Name cannot be longer than 20 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot be longer than 100 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(max = 10, message = "password cannot be longer than 10 characters")
    private String password;

    @NotBlank(message = "Phone number is mandatory")
    @Size(max = 10, message = "Phone number cannot be longer than 10 characters")
    @Column(name = "mobile_number")
    private String mobileNumber;
}
