package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class SessionServiceUnitTest {
    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    private Teacher teacher;
    private User user1;
    private User user2;
    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void init() {

        teacher = new Teacher();
        teacher.setId(5L);

        user1 = new User();
        user1.setId(2L);
        user2 = new User();
        user2.setId(3L);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Long sessionId = 1L;
        String name = "testName";
        String description = "testDescription";
        Date date = new Date();

        session = new Session();
        session.setId(sessionId);
        session.setName(name);
        session.setDescription(description);
        session.setDate(date);
        session.setTeacher(teacher);
        session.setUsers(users);

        List<Long> usersId = new ArrayList<>();
        usersId.add(user1.getId());
        usersId.add(user2.getId());

        sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName(name);
        sessionDto.setDescription(description);
        sessionDto.setDate(date);
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(usersId);
    }

    @Test
    public void toDto_whenSessionIsValid_thenReturnsSessionDto() {
        SessionDto testSessionDto = sessionMapper.toDto(session);

        assertEquals(sessionDto, testSessionDto);
    }

    @Test
    public void toDto_whenSessionIsNull_thenReturnsNull() {
        Session session = null;

        SessionDto testSessionDto = sessionMapper.toDto(session);

        assertNull(testSessionDto);
    }

    @Test
    public void toDto_whenSessionHasNullTeacher_thenReturnsDtoWithNullTeacherId() {
        session.setTeacher(null);

        SessionDto testSessionDto = sessionMapper.toDto(session);
        assertNull(testSessionDto.getTeacher_id());
    }

    @Test
    public void toDto_whenSessionHasNullTeacherId_thenReturnsDtoWithNullTeacherId() {
        session.getTeacher().setId(null);

        SessionDto testSessionDto = sessionMapper.toDto(session);
        assertNull(testSessionDto.getTeacher_id());
    }

    @Test
    public void toEntity_whenSessionDtoIsValid_thenReturnsSession() {
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);

        Session testSession = sessionMapper.toEntity(sessionDto);

        verify(teacherService).findById(teacher.getId());
        verify(userService).findById(user1.getId());
        verify(userService).findById(user2.getId());
        assertEquals(session, testSession);
    }

    @Test
    public void toEntity_whenSessionDtoIsNull_thenReturnsNull() {
        SessionDto sessionDto = null;

        Session testSession = sessionMapper.toEntity(sessionDto);

        assertNull(testSession);
    }

    @Test
    public void toDtoList_whenSessionListIsValid_thenReturnsSessionDtoList() {
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionDto);

        List<SessionDto> testSessionDtoList = sessionMapper.toDto(sessionList);

        assertEquals(sessionDtoList, testSessionDtoList);
    }

    @Test
    public void toDtoList_whenSessionListIsNull_thenReturnsNull() {
        List<Session> sessionList = null;

        List<SessionDto> testSessionDtoList = sessionMapper.toDto(sessionList);

        assertNull(testSessionDtoList);
    }

    @Test
    public void toEntityList_whenSessionDtoListIsValid_thenReturnsSessionList() {
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionDto);

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);

        List<Session> testSessionList = sessionMapper.toEntity(sessionDtoList);

        verify(teacherService).findById(teacher.getId());
        verify(userService).findById(user1.getId());
        verify(userService).findById(user2.getId());
        assertEquals(sessionList, testSessionList);
    }

    @Test
    public void toEntityList_whenSessionDtoListIsNull_thenReturnsNull() {
        List<SessionDto> sessionDtoList = null;

        List<Session> testSessionList = sessionMapper.toEntity(sessionDtoList);

        assertNull(testSessionList);
    }

    @Test
    public void hashCode_whenSessionsAreEqual_thenReturnsSameHashCode() {
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(1L);

        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    public void hashCode_whenSessionsAreNotEqual_thenReturnsDifferentHashCode() {
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(2L);

        assertNotEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    public void setCreatedAt_whenCalled_thenSetsCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        session.setCreatedAt(now);
        assertEquals(now, session.getCreatedAt());
    }

    @Test
    public void setUpdatedAt_whenCalled_thenSetsUpdatedAt() {
        LocalDateTime now = LocalDateTime.now();
        session.setUpdatedAt(now);
        assertEquals(now, session.getUpdatedAt());
    }
}
