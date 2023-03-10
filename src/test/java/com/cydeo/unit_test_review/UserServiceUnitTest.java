package com.cydeo.unit_test_review;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import com.cydeo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keyclockService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    @Spy
    private UserMapper userMapper = new UserMapper(new ModelMapper());

    User user;
    UserDTO userDTO;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);
        userDTO.setRole(new RoleDTO(null, "Manager"));

    }

    private List<User> getUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");
        return List.of(user, user2);
    }

    private List<UserDTO> getUserDTOs() {
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        return List.of(userDTO, userDTO2);
    }

    @Test
    void should_list_all_users() {
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers());
        List<UserDTO> expectedList = getUserDTOs();

        List<UserDTO> actualList = userService.listAllUsers();

        assertThat(actualList).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(expectedList);

    }

    @Test
    void should_find_user_by_userName() {
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        UserDTO actual = userService.findByUserName(user.getUserName());
        assertThat(actual).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(userDTO);
    }


    @Test
    void should_throw_exception_when_User_not_found() {

//        Throwable throwable = catchThrowable(() -> userService.findByUserName("SomeUserNmae"));
//        assertInstanceOf(NoSuchElementException.class, throwable);
//        assertEquals("User not found", throwable.getMessage());

        assertThrowsExactly(NoSuchElementException.class, () -> userService.findByUserName(null), "User not JKLJK found");
    }

    @Test
    void should_save_user() {

        when(userRepository.save(any())).thenReturn(user);
        UserDTO actualDTO = userService.save(userDTO);
        assertThat(actualDTO).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(userDTO);
        verify(passwordEncoder).encode(anyString());

    }


    @Test
    void should_update_user() {
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        UserDTO actualDTO = userService.update(userDTO);
        verify(passwordEncoder).encode(anyString());
        assertThat(actualDTO.getPassWord()).isBase64();
    }

    @Test
    void should_delete_manager() throws TicketingProjectException {
        User user= getUser("Manager");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());
        userService.delete(userDTO.getUserName());
        assertThat(user.getIsDeleted()).isTrue();
        assertTrue(user.getIsDeleted());
        assertNotEquals("user3", user.getUserName());
    }

    @Test
    void should_delete_employee() throws TicketingProjectException {
        User user= getUser("Employee");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());
        userService.delete(userDTO.getUserName());
        assertTrue(user.getIsDeleted());
        assertNotEquals("user3", user.getUserName());
    }

    @ParameterizedTest
    @ValueSource(strings={"Manager", "Employee"})
    void should_delete_user() throws TicketingProjectException {
        User user= getUser("Employee");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        lenient().when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());
        lenient().when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn( List.of(new ProjectDTO()));
        userService.delete(userDTO.getUserName());
        assertTrue(user.getIsDeleted());
        assertNotEquals("user3", user.getUserName());
    }

    @Test
    void should_throw_exception_when_delete_manager() throws TicketingProjectException {
        User user= getUser("Manager");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);

        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn( List.of(new ProjectDTO()));

        Throwable throwable= catchThrowable(()-> userService.delete(userDTO.getUserName()));
        assertInstanceOf(TicketingProjectException.class, throwable);

        assertEquals("User cannot be deleted", throwable.getMessage());
        verify(userMapper, atLeastOnce()).convertToDto(any());
    }

    private User getUser(String role){
        User user3= new User();

        user3.setUserName("user3");
        user3.setEnabled(false);
        user3.setIsDeleted(false);
        user3.setRole(new Role(role));
        return user3;
    }
}
