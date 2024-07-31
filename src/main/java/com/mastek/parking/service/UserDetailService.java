package com.mastek.parking.service;

import com.mastek.parking.common.ApiResponse;
import com.mastek.parking.dto.LoginRequestDto;
import com.mastek.parking.dto.UserDto;
import com.mastek.parking.dto.UserUpdateDto;
import com.mastek.parking.exception.UserNotFoundException;
import com.mastek.parking.model.User;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserDetailService {
    User addUser(UserDto userDto);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User getUserByEmail(String email);

    public boolean canBookSlot(Long userId);

    boolean isValidOfficialEmail(Long userId);

    User updateUser(String email, UserUpdateDto userUpdateDto);

    public String login(LoginRequestDto loginRequest);

    public void logout(String token);

    public String forgotPassword(String email);

    public void resetPassword(String token, String newPassword);
}
