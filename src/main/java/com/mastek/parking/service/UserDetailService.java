package com.mastek.parking.service;

import com.mastek.parking.dto.UserDto;
import com.mastek.parking.model.User;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserDetailService {
    String addUser(UserDto userDto);

    List<User> getAllUsers();
}
