package com.openclassrooms.starterjwt.mappers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
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
public class UserMapperUnitTest {

    @InjectMocks
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    User user;
    private UserDto userDto;

    @BeforeEach
    public void init() {
        Long userId = 1L;
        String username = "yoga@studio.com";
        String lastName = "Studio";
        String firstName = "Yoga";
        String password = "test!1234";
        boolean admin = true;

        user = new User();
        user.setId(userId);
        user.setEmail(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setAdmin(admin);

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail(username);
        userDto.setLastName(lastName);
        userDto.setFirstName(firstName);
        userDto.setPassword(password);
        userDto.setAdmin(admin);
    }

    @Test
    public void toDto_whenUserIsValid_thenReturnsUserDto() {
        UserDto testUserDto = userMapper.toDto(user);

        assertEquals(userDto, testUserDto);
    }

    @Test
    public void toDto_whenUserIsNull_thenReturnsNull() {
        User user = null;

        UserDto testUserDto = userMapper.toDto(user);

        assertNull(testUserDto);
    }

    @Test
    public void toEntity_whenUserDtoIsValid_thenReturnsUser() {
        User testUser = userMapper.toEntity(userDto);

        assertEquals(user, testUser);
    }

    @Test
    public void toEntity_whenUserDtoIsNull_thenReturnsNull() {
        UserDto userDto = null;

        User testUser = userMapper.toEntity(userDto);

        assertNull(testUser);
    }

    @Test
    public void toDtoList_whenUserListIsValid_thenReturnsUserDtoList() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        List<UserDto> testUserDtoList = userMapper.toDto(userList);

        assertEquals(userDtoList, testUserDtoList);
    }

    @Test
    public void toDtoList_whenUserListIsNull_thenReturnsNull() {
        List<User> userList = null;

        List<UserDto> testUserDtoList = userMapper.toDto(userList);

        assertNull(testUserDtoList);
    }

    @Test
    public void toEntityList_whenUserDtoListIsValid_thenReturnsUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        List<User> testUserList = userMapper.toEntity(userDtoList);

        assertEquals(userList, testUserList);
    }

    @Test
    public void toEntityList_whenUserDtoListIsNull_thenReturnsNull() {
        List<UserDto> userList = null;

        List<User> testUserList = userMapper.toEntity(userList);

        assertNull(testUserList);
    }

}
