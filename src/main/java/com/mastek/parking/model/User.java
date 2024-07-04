package com.mastek.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mastek.parking.dto.UserDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "\"user_details\"") // Specify the exact table name with quotes
@Data

public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "username")
    private String username;

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
    @JsonIgnore // Prevents serialization of password field
    private String password;

    @NotBlank(message = "Phone number is mandatory")
    @Size(max = 10, message = "Phone number cannot be longer than 10 characters")
    @Column(name = "mobile_number")
    private String mobileNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Column(name = "updated_date")
    private Date updatedDate;


    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
        updatedDate = new Date();

    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }


}
