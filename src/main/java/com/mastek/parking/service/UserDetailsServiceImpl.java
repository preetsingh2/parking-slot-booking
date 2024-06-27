package com.mastek.parking.service;

import com.mastek.parking.dto.UserDto;
import com.mastek.parking.model.User;
import com.mastek.parking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailService{

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private Validator validator;
    @Override
    @Transactional
    public String addUser(UserDto userDto) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        //verify user is existing
        if(!checkExistingUser(userDto)){
        // Convert UserDto to User entity
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setMobileNumber(userDto.getMobileNumber());
        userRepository.save(user);
            return "User added successfully";
        }else{
            return "User is already existing, try login!";
        }
    }

    @Override
    @Transactional()
    public List<User> getAllUsers() {return userRepository.findAll();
    }

    private boolean checkExistingUser(UserDto userDto){
        return userRepository.findByEmail(userDto.getEmail()) != null;
    }
}
