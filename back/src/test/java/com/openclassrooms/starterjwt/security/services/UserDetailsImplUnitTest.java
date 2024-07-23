package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserDetailsImplUnitTest {

    private UserDetails userDetails1;
    private UserDetails userDetails2;
    private UserDetails userDetails3;

    @BeforeEach
    public void init() {
        userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("john@example.com")
                .lastName("Wick")
                .firstName("John")
                .admin(true)
                .password("Coucou12@")
                .build();

        userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("bella@example.com")
                .lastName("Bella")
                .firstName("Anna")
                .admin(true)
                .password("Coucou12@!")
                .build();

        userDetails3 = UserDetailsImpl.builder()
                .id(3L)
                .username("jean@example.com")
                .lastName("Dupont")
                .firstName("Jean")
                .admin(false)
                .password("Coucou12@!2")
                .build();

    }

    @Test
    public void getAdmin_whenUserIsAdmin_thenReturnsTrue() {
        UserDetailsImpl userDetailsImpl1 = (UserDetailsImpl) userDetails1;
        assertEquals(true, userDetailsImpl1.getAdmin());
    }

    @Test
    public void equals_whenUsersAreEqual_thenReturnsTrue() {
        Boolean testEqual = userDetails1.equals(userDetails2);
        assertEquals(true, testEqual);
    }

    @Test
    public void equals_whenUsersAreNotEqual_thenReturnsFalse() {
        Boolean testEqual = userDetails1.equals(userDetails3);
        assertEquals(false, testEqual);
    }

    @Test
    public void equals_whenUserIsNull_thenReturnsFalse() {
        Boolean testEqual = userDetails1 == null;
        assertEquals(false, testEqual);
    }

    @Test
    public void toStringBuilder_whenCalled_thenReturnsStringRepresentation() {
        UserDetailsImpl.UserDetailsImplBuilder builder = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .lastName("testLastName")
                .firstName("testFirstName")
                .admin(true)
                .password("Coucou123@");

        String expected = "UserDetailsImpl.UserDetailsImplBuilder(id=1, username=test@example.com, firstName=testFirstName, lastName=testLastName, admin=true, password=Coucou123@)";
        assertEquals(expected, builder.toString());
    }
}
