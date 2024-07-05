package com.mastek.parking.service;

import com.mastek.parking.common.ApiResponse;
import com.mastek.parking.dto.UserDto;
import com.mastek.parking.model.User;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserDetailService {
    User addUser(UserDto userDto);

    List<User> getAllUsers();

    User getUserById(Long userId);

    public boolean canBookSlot(Long userId);

    boolean isValidOfficialEmail(Long userId);
}
