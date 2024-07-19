package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private Long sessionId = 1L;
    private Long userId = 2L;
    final ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

    @BeforeEach
    public void init() {
        List<User> userList = new ArrayList<>();

        session = new Session();
        session.setId(sessionId);
        session.setName("Yoga");
        session.setUsers(userList);
        session.setDescription("test description");

        sessionCaptor.getAllValues().clear();
    }

    @Test
    public void createSession_whenValidSession_thenReturnsSession() {
        // given
        when(sessionRepository.save(session)).thenReturn(session);

        // when
        sessionService.create(session);

        // then
        verify(sessionRepository).save(session);
    }

    @Test
    public void deleteSession_whenValidId_thenDeletesSession() {
        // given
        // No setup needed

        // when
        sessionService.delete(sessionId);

        // then
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void findAllSessions_whenCalled_thenReturnsSessionList() {
        // given
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        when(sessionRepository.findAll()).thenReturn(sessionList);

        // when
        List<Session> foundSessions = sessionService.findAll();

        // then
        verify(sessionRepository).findAll();
        assertEquals(sessionList, foundSessions);
    }

    @Test
    public void findSessionById_whenValidId_thenReturnsSession() {
        // given
        Optional<Session> sessionOptional = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptional);

        // when
        Session foundSession = sessionService.getById(sessionId);

        // then
        verify(sessionRepository).findById(sessionId);
        assertEquals(session.getId(), foundSession.getId());
        assertEquals(session.getName(), foundSession.getName());
        assertEquals(session.getDescription(), foundSession.getDescription());

    }

    @Test
    public void findSessionById_whenInvalidId_thenReturnsNull() {
        // given
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // when
        Session foundSession = sessionService.getById(sessionId);

        // then
        verify(sessionRepository).findById(sessionId);
        assertNull(foundSession);
    }

    @Test
    public void updateSession_whenValidSession_thenUpdatesAndReturnsSession() {
        // given
        Session updatedSession = new Session();
        updatedSession.setId(2L);
        updatedSession.setName("Meditation");

        // when
        sessionService.update(sessionId, updatedSession);

        // then
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(sessionId, sessionCaptor.getValue().getId());
        assertEquals(updatedSession.getName(), sessionCaptor.getValue().getName());
    }

    @Test
    public void participateSession_whenValidUser_thenAddsUserToSession() {
        // given
        User user = new User();
        user.setId(userId);
        user.setEmail("test@studio.com");

        Optional<User> userOptional = Optional.of(user);
        Optional<Session> sessionOptional = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptional);
        when(userRepository.findById(userId)).thenReturn(userOptional);

        // when
        sessionService.participate(sessionId, userId);

        // then
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(sessionCaptor.getValue().getUsers().get(0), user);
    }

    @Test
    public void participateSession_whenSessionOrUserNotFound_thenThrowsNotFoundException() {
        // given
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void participateSession_whenUserAlreadyParticipating_thenThrowsBadRequestException() {
        // given
        User user = new User();
        user.setId(userId);
        user.setEmail("test@studio.com");

        session.getUsers().add(user);

        Optional<User> userOptional = Optional.of(user);
        Optional<Session> sessionOptional = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptional);
        when(userRepository.findById(userId)).thenReturn(userOptional);

        // when
        // then
        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void unParticipateSession_whenValidUser_thenRemovesUserFromSession() {
        // given
        User user = new User();
        user.setId(userId);
        user.setEmail("test@studio.com");

        session.getUsers().add(user);

        Optional<Session> sessionOptional = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptional);

        // when
        sessionService.noLongerParticipate(sessionId, userId);

        // then
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertTrue(sessionCaptor.getValue().getUsers().isEmpty());
    }

    @Test
    public void unParticipateSession_whenSessionNotFound_thenThrowsNotFoundException() {
        // given
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void unParticipateSession_whenUserNotParticipating_thenThrowsBadRequestException() {
        // given
        Optional<Session> sessionOptional = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptional);

        // when
        // then
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }
}
