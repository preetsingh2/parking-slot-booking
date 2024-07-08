package com.mastek.parking.service;

import com.mastek.parking.dto.UserDto;
import com.mastek.parking.dto.UserUpdateDto;
import com.mastek.parking.exception.ExistingUserException;
import com.mastek.parking.exception.InvalidOfficialEmailException;
import com.mastek.parking.exception.UserNotFoundException;
import com.mastek.parking.model.User;
import com.mastek.parking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static com.mastek.parking.common.Constants.OFFICIAL_EMAIL_PATTERN;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User addUser(UserDto userDto) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        //validate official email id
        validateOfficialEmail(userDto.getEmail());

        //verify user is existing
        if (checkExistingUser(userDto)) {
            throw new ExistingUserException("Existing User");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setMobileNumber(userDto.getMobileNumber());
        user.setStatus("Active");
        return userRepository.save(user);
    }

    @Override
    @Transactional()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));


        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }
        if (userUpdateDto.getMobileNumber()!= null) {
            user.setMobileNumber(userUpdateDto.getMobileNumber());
        }

        if (userUpdateDto.getStatus() != null) {
            user.setStatus(userUpdateDto.getStatus());
        }

        userRepository.save(user);
    }

    private boolean checkExistingUser(UserDto userDto) {
        return userRepository.findByEmail(userDto.getEmail()).isPresent() ;
    }

    @Override
    @Transactional
    public User getUserById(Long userId) {
        // Retrieve user by ID from the database
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    private void validateOfficialEmail(String email) {
        if (!Pattern.matches(OFFICIAL_EMAIL_PATTERN, email)) {
            throw new InvalidOfficialEmailException("Invalid official email address: " + email);
        }
    }

    @Override
    public boolean canBookSlot(Long userId) {
        User user = getUserById(userId); // This will throw exception if user not found
        // Example: Check if user has a valid official email
        boolean hasValidOfficialEmail = isValidOfficialEmail(userId);
        // Additional business logic to determine if user can book a slot
        return hasValidOfficialEmail; // Replace with your logic
    }

    @Override
    public boolean isValidOfficialEmail(Long userId) {
        User user = getUserById(userId);
        String officialEmailDomain = "@mastek.com";
        String userEmail = user.getEmail();
        return userEmail != null && userEmail.toLowerCase().endsWith(officialEmailDomain);
    }

}
