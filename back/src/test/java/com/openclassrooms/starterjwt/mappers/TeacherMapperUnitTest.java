package com.openclassrooms.starterjwt.mappers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class TeacherMapperUnitTest {

    @InjectMocks
    private TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void init() {
        Long teacherId = 1L;
        String lastName = "lastName";
        String firstName = "firstName";

        teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);

        teacherDto = new TeacherDto();
        teacherDto.setId(teacherId);
        teacherDto.setLastName(lastName);
        teacherDto.setFirstName(firstName);
    }

    @Test
    public void toDto_whenTeacherIsValid_thenReturnsTeacherDto() {
        TeacherDto testTeacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacherDto, testTeacherDto);
    }

    @Test
    public void toDto_whenTeacherIsNull_thenReturnsNull() {
        Teacher teacher = null;

        TeacherDto testTeacherDto = teacherMapper.toDto(teacher);

        assertNull(testTeacherDto);
    }

    @Test
    public void toEntity_whenTeacherDtoIsValid_thenReturnsTeacher() {
        Teacher testTeacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacher, testTeacher);
    }

    @Test
    public void toEntity_whenTeacherDtoIsNull_thenReturnsNull() {
        TeacherDto teacherDto = null;

        Teacher testTeacher = teacherMapper.toEntity(teacherDto);

        assertNull(testTeacher);
    }

    @Test
    public void toDtoList_whenTeacherListIsValid_thenReturnsTeacherDtoList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        List<TeacherDto> testTeacherDtoList = teacherMapper.toDto(teacherList);

        assertEquals(teacherDtoList, testTeacherDtoList);
    }

    @Test
    public void toDtoList_whenTeacherListIsNull_thenReturnsNull() {
        List<Teacher> teacherList = null;

        List<TeacherDto> testTeacherDtoList = teacherMapper.toDto(teacherList);

        assertNull(testTeacherDtoList);
    }

    @Test
    public void toEntityList_whenTeacherDtoListIsValid_thenReturnsTeacherList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        List<Teacher> testTeacherList = teacherMapper.toEntity(teacherDtoList);

        assertEquals(teacherList, testTeacherList);
    }

    @Test
    public void toEntityList_whenTeacherDtoListIsNull_thenReturnsNull() {
        List<TeacherDto> teacherDtoList = null;

        List<Teacher> testTeacherList = teacherMapper.toEntity(teacherDtoList);

        assertNull(testTeacherList);
    }

}
