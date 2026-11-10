package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;





import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService; // veya UserService userService;

    @Test
    void loadUserByUsername_Success() {
        User user = new User();
        user.setUsername("ayse");
        given(userRepository.findByUsername("ayse")).willReturn(Optional.of(user));
        UserDetails result = userService.loadUserByUsername("ayse");

        assertNotNull(result);
        assertEquals("ayse", result.getUsername());
    }
    @Test
    void findByUsername() {
        User user = new User();
        user.setUsername("ayse");
        user.setPassword("ayse");
        user.setEmail("ayse@gmail.com");
        user.setLastName("ayse");
        user.setFirstName("ayse");


        given(userRepository.findByUsername("ayse")).willReturn(Optional.of(user));
        assertEquals(userRepository.findByUsername("ayse").get(), user);

    }

    @Test
    void existsByUsername() {
        given(userRepository.existsByUsername("ayse")).willReturn(true);
        boolean exists = userService.existsByUsername("ayse");

        assertTrue(exists);
    }

    @Test
    void existsByEmail() {
    }
}