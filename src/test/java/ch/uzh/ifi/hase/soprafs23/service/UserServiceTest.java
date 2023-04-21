package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setUserId(1L);
    testUser.setEmail("testName");
    testUser.setUsername("testUsername");
    testUser.setPassword("123");
    testUser.setToken(null);
    testUser.setStatus(UserStatus.OFFLINE);

    // when -> any object is being saved in the userRepository -> return the dummy
    // testUser
    when(userRepository.save(any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being saved in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    verify(userRepository, Mockito.times(1)).save(any());

    assertEquals(testUser.getUserId(), createdUser.getUserId());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateName_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    when(userRepository.findByUsername(any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    when(userRepository.findByUsername(any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }
    @Test
    void loginUser_ShouldReturnLoggedInUser() {
        // Mock the necessary method calls
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        // Call the method to be tested
        User loggedInUser = userService.loginUser(testUser);

        // Assert that the returned user is the same as the test user
        assertEquals(testUser, loggedInUser);
        assertNotNull(loggedInUser.getToken());
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }
    @Test
    void loginUser_ShouldThrowNotFoundException_WhenUserNotFound() {
        // Mock the necessary method calls
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        // Call the method to be tested and assert that it throws the expected exception
        assertThrows(ResponseStatusException.class, () -> {
            userService.loginUser(testUser);
        }, "The username provided was not found");
    }
    @Test
    void loginUser_ShouldThrowBadRequestException_WhenWrongPassword() {
        // Create a user with the wrong password
        User wrongPasswordUser = new User();
        wrongPasswordUser.setUserId(1L);
        wrongPasswordUser.setUsername("testUsername");
        wrongPasswordUser.setPassword("wrongPassword");
        wrongPasswordUser.setToken(null);
        wrongPasswordUser.setStatus(UserStatus.OFFLINE);

        // Mock the necessary method calls
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        // Call the method to be tested and assert that it throws the expected exception
        assertThrows(ResponseStatusException.class, () -> {
            userService.loginUser(wrongPasswordUser);
        }, "Wrong password");
    }
    @Test
    void loginUser_ShouldThrowResponseStatusException_WhenUserNotFound() {

        // Set up mock repository
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(null);

        // Call the method to be tested and assert that it throws the expected exception
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser));

        // Verify that the findByUsername method was called with the expected argument
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByUsername(usernameCaptor.capture());
        assertEquals(testUser.getUsername(), usernameCaptor.getValue());
    }

    @Test
    void getUser_ShouldThrowExceptionWhenUnknownUser() {

        // Mock the UserRepository to return a 404 NOT FOUND response when getUser is called with an unknown user ID
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method to be tested
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUser(testUser.getUserId());
        });

        // Verify that the exception has a 404 NOT FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

}
