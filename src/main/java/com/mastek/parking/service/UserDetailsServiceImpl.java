package com.mastek.parking.service;

import com.mastek.parking.common.TokenProvider;
import com.mastek.parking.dto.LoginRequestDto;
import com.mastek.parking.dto.UserDto;
import com.mastek.parking.dto.UserUpdateDto;
import com.mastek.parking.exception.*;
import com.mastek.parking.model.User;
import com.mastek.parking.repository.UserRepository;
import com.mastek.parking.util.EmailNotification;
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
import java.util.UUID;
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

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailNotification emailService;


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
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setMobileNumber(userDto.getMobileNumber());
        user.setStatus("Active");
        log.info("Login successful");
        return userRepository.save(user);
    }

    @Override
    @Transactional()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional()
    public User updateUser(String email, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + email));


        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }
        if (userUpdateDto.getMobileNumber()!= null) {
            user.setMobileNumber(userUpdateDto.getMobileNumber());
        }

        if (userUpdateDto.getStatus() != null) {
            user.setStatus(userUpdateDto.getStatus());
        }
        log.info("User details updated successfully");
        return userRepository.save(user);
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

    @Override
    @Transactional
    public User getUserByEmail(String email) {
        // Retrieve user by email from the database
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + email));
    }

    private void validateOfficialEmail(String email) {
        if (!Pattern.matches(OFFICIAL_EMAIL_PATTERN, email)) {
            throw new InvalidOfficialEmailException("Invalid official email address: " + email);
        }
    }


    @Override
    @Transactional
    public String login(LoginRequestDto loginRequest) throws AuthenticationException {
        User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new AuthenticationException("User not found"));


        if (!user.getEmail().equalsIgnoreCase(loginRequest.getEmail())) {
            log.info("Invalid username");
            throw new AuthenticationException("Invalid username");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("Invalid password");
            throw new AuthenticationException("Invalid password");
        }
        log.info("Login Successful");
        log.info("Token generated! " +tokenProvider.generateToken(user));
        // Generate JWT token
        return tokenProvider.generateToken(user);
    }

    @Override
    @Transactional
    public void logout(String token) {
        // Invalidate the token
        tokenProvider.invalidateToken(token);
    }

    @Override
    @Transactional
    public String forgotPassword(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Generate a password reset token
        String resetToken = UUID.randomUUID().toString();

        // Save the reset token to the user record or a separate password reset token entity
        user.setResetToken(resetToken);
        userRepository.save(user);

        // Create a password reset link
        String resetLink = "http://gmail.com/reset-password?token=" + resetToken;

        // Email content
        String subject = "Password Reset Request";
        String body = "Dear " + user.getFirstName() + ",\n\n" +
                "We received a request to reset your password. Click the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Admin";

        // Send the email
        emailService.sendEmail(email, subject, body);
        log.info("reset email is sent");
        return resetLink;
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) throws InvalidTokenException, UserNotFoundException {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        // Update the user's password and clear the reset token
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        log.info("Password reset is successful");
    }
}
