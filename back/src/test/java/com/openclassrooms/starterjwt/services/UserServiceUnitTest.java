package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Long userId = 1L;

    @Test
    public void findById_whenUserExists_returnsUser() {
        // given
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User testUser = userService.findById(userId);

        // then
        verify(userRepository).findById(userId);
        assertEquals(user, testUser);
    }

    @Test
    public void findById_whenUserDoesNotExist_returnsNull() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        User testUser = userService.findById(userId);

        // then
        verify(userRepository).findById(userId);
        assertNull(testUser);
    }

    @Test
    public void deleteById_whenUserExists_deletesUser() {
        // given

        // when
        userService.delete(userId);

        // then
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void deleteById_whenUserDoesNotExist_noExceptionThrown() {
        // given
        doNothing().when(userRepository).deleteById(userId);

        // when
        userService.delete(userId);

        // then
        verify(userRepository).deleteById(userId);
        // No exception should be thrown
    }
}
