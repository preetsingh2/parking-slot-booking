package com.mastek.parking.service;

import com.mastek.parking.common.TokenProvider;
import com.mastek.parking.dto.LoginRequestDto;
import com.mastek.parking.dto.UserDto;
import com.mastek.parking.model.User;
import com.mastek.parking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserDetailService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // Mock data
        LoginRequestDto loginRequest = new LoginRequestDto("preeti@mastek.com", "hello123");

        // Create a mock user with the same email
        mockUser = new User();
        mockUser.setEmail(loginRequest.getEmail());
        mockUser.setPassword(loginRequest.getPassword());

        // Mock repository behavior
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(mockUser));

        System.out.println("loginRequest email : " +loginRequest.getEmail() +" and Password "+loginRequest.getPassword());
        System.out.println("user email : " +mockUser.getEmail() +" and Password "+mockUser.getPassword());

        // Call the service method
        String token = userService.login(loginRequest);

        // Verify that a token is returned
        assertNotNull(token);
    }

    /*@Test
    void testAddUser() {
        // Mock data
        UserDto userDto = new UserDto("user@example.com", "password123");
        User user = new User(userDto.getEmail(), userDto.getPassword());

        // Mock repository behavior
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method
        User addedUser = userDetailService.addUser(userDto);

        // Verify that the user was added
        assertNotNull(addedUser);
        assertEquals(userDto.getEmail(), addedUser.getEmail());
        assertEquals(userDto.getPassword(), addedUser.getPassword());
    }

    @Test
    void testGetAllUsers() {
        // Mock data
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1@example.com", "pass1"));
        userList.add(new User("user2@example.com", "pass2"));

        // Mock repository behavior
        when(userRepository.findAll()).thenReturn(userList);

        // Call the service method
        List<User> fetchedUsers = userDetailService.getAllUsers();

        // Verify that the list is not empty
        assertFalse(fetchedUsers.isEmpty());
        assertEquals(userList.size(), fetchedUsers.size());
    }
    */


}
