package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceUnitTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Long teacherId = 1L;
    private Teacher teacher;

    @BeforeEach
    public void init() {
        teacher = new Teacher();
        teacher.setId(teacherId);
    }

    @Test
    public void findAll_whenCalled_returnsListOfTeachers() {
        // given
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        when(teacherRepository.findAll()).thenReturn(teacherList);

        // when
        List<Teacher> foundTeachers = teacherService.findAll();

        // then
        verify(teacherRepository).findAll();
        assertEquals(teacherList, foundTeachers);
    }

    @Test
    public void findById_whenTeacherExists_returnsTeacher() {
        // given
        Optional<Teacher> teacherOptional = Optional.of(teacher);

        when(teacherRepository.findById(teacherId)).thenReturn(teacherOptional);

        // when
        Teacher testTeacher = teacherService.findById(teacherId);

        // then
        verify(teacherRepository).findById(teacherId);
        assertEquals(teacher, testTeacher);
    }

    @Test
    public void findById_whenTeacherDoesNotExist_returnsNull() {
        // given
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // when
        Teacher testTeacher = teacherService.findById(teacherId);

        // then
        verify(teacherRepository).findById(teacherId);
        assertNull(testTeacher);
    }
}
