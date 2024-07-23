package com.openclassrooms.starterjwt.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mapstruct.factory.Mappers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionMapperUnitTest {

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
    public void toDto_whenValidSession_thenReturnsDto() {
        SessionDto testSessionDto = sessionMapper.toDto(session);

        assertEquals(sessionDto, testSessionDto);
    }

    @Test
    public void toDto_whenNullSession_thenReturnsNull() {
        Session session = null;

        SessionDto testSessionDto = sessionMapper.toDto(session);

        assertNull(testSessionDto);
    }

    @Test
    public void toDto_whenSessionWithNullTeacher_thenReturnsDtoWithNullTeacherId() {
        session.setTeacher(null);

        SessionDto testSessionDto = sessionMapper.toDto(session);
        assertNull(testSessionDto.getTeacher_id());
    }

    @Test
    public void toDto_whenSessionWithNullTeacherId_thenReturnsDtoWithNullTeacherId() {
        session.getTeacher().setId(null);

        SessionDto testSessionDto = sessionMapper.toDto(session);
        assertNull(testSessionDto.getTeacher_id());
    }

    @Test
    public void toEntity_whenNullDto_thenReturnsNull() {
        SessionDto sessionDto = null;

        Session testSession = sessionMapper.toEntity(sessionDto);

        assertNull(testSession);
    }

    @Test
    public void toDtoList_whenValidSessionList_thenReturnsDtoList() {
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionDto);

        List<SessionDto> testSessionDtoList = sessionMapper.toDto(sessionList);

        assertEquals(sessionDtoList, testSessionDtoList);
    }

    @Test
    public void toDtoList_whenNullSessionList_thenReturnsNull() {
        List<Session> sessionList = null;

        List<SessionDto> testSessionDtoList = sessionMapper.toDto(sessionList);

        assertNull(testSessionDtoList);
    }

    @Test
    public void toEntityList_whenNullDtoList_thenReturnsNull() {
        List<SessionDto> sessionDtoList = null;

        List<Session> testSessionList = sessionMapper.toEntity(sessionDtoList);

        assertNull(testSessionList);
    }
}
