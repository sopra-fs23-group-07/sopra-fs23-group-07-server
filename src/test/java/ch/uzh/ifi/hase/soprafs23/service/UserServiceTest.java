package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    testUser.setEmail("test@Name");
    testUser.setUsername("testUsername");
    testUser.setPassword("123");
    testUser.setToken(null);
    testUser.setStatus(UserStatus.OFFLINE);

    // when -> any object is being saved in the userRepository -> return the dummy
    // testUser
    when(userRepository.save(any())).thenReturn(testUser);
  }
    @Test
    void getEvents_ShouldReturnListOfUsers() {
        // Create some test users
        User testUser2 = new User();
        testUser2.setUserId(2L);
        List<User> testUsers = Arrays.asList(testUser, testUser2);

        // Mock the repository method call to return the test events
        when(userRepository.findAll()).thenReturn(testUsers);

        // Call the method to be tested
        List<User> returnedUsers = userService.getUsers();

        // Verify that the returned events match the test events
        assertEquals(testUsers, returnedUsers);
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

        // Create a second user with the same username
        User inputUser = new User();
        inputUser.setUserId(1L);
        inputUser.setUsername("testUsername");  // Duplicate username
        inputUser.setEmail("test@Name");  // Duplicate email

        // when -> setup additional mocks for UserRepository
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        // then -> attempt to create the second user with the same username -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(inputUser));
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
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser), "The username provided was not found");
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
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(wrongPasswordUser), "Wrong password");
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
    void loginUser_ShouldThrowResponseStatusException_WhenUserNotLoggedOut() {

        testUser.setStatus(UserStatus.ONLINE);

        // Set up mock repository
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

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
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.getUser(testUser.getUserId()));

        // Verify that the exception has a 404 NOT FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    void getUser_ShouldReturnUserById() {

        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // Call the method to be tested
        User retrievedUser = userService.getUser(testUser.getUserId());

        // Assert that the captured user ID matches the ID of the test user
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());
        // Assert that the returned user is the same as the test user
        assertEquals(testUser, retrievedUser);
    }
    @Test
    void getUser_UserNotFound() {

        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(null);

        // Call the method to be tested and assert that it throws the expected exception
        assertThrows(ResponseStatusException.class, () -> userService.getUser(testUser.getUserId()));
    }
    @Test
    void logoutUser_ShouldSetUserStatusToOffline() {

        // Mock the necessary method call
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // Call the method to be tested
        userService.logoutUser(testUser.getUserId());

        // Assert that the user's status is set to offline
        assertEquals(UserStatus.OFFLINE, testUser.getStatus());
    }
    @Test
    void updateUser_ShouldUpdateUserFields() {

        // Create an input user with updated fields
        User inputUser = new User();
        inputUser.setUserId(1L);
        inputUser.setUsername("updatedTestUser");
        inputUser.setPassword("updatedTestPassword");
        inputUser.setEmail("updatedTest@example.com");
        inputUser.setBio("BIO");
        inputUser.setBirthdate(LocalDate.parse("2000-05-05"));

        // Mock the necessary method calls
        when(userRepository.findByUserId(inputUser.getUserId())).thenReturn(testUser);

        // Call the method to be tested
        userService.updateUser(inputUser);

        // Verify that the save method was called with the correct user object
        verify(userRepository).save(testUser);

        // Assert that the user fields were updated correctly
        assertEquals(inputUser.getUsername(), testUser.getUsername());
        assertEquals(inputUser.getPassword(), testUser.getPassword());
        assertEquals(inputUser.getEmail(), testUser.getEmail());
        assertEquals(inputUser.getBio(), testUser.getBio());
        assertEquals(inputUser.getBirthdate(), testUser.getBirthdate());
    }
    @Test
    void updateUser_ShouldNotUpdateUsername() {

        // Create an input user with updated fields
        User inputUser = new User();
        inputUser.setUserId(1L);
        inputUser.setUsername("testUsername");
        inputUser.setEmail("test@Name");

        // Mock the necessary method calls
        when(userRepository.findByUserId(inputUser.getUserId())).thenReturn(testUser);

        // Call the method to be tested
        userService.updateUser(inputUser);

        // Assert that the user fields are the same
        assertEquals(inputUser.getUsername(), testUser.getUsername());
    }
    @Test
    void updateUser_DuplicateUsernameOrEmail_ThrowsException() {
        // Create an existing user with a duplicate username
        User existingUser = new User();
        existingUser.setUserId(2L);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");

        // Create the input user with updated username and email
        User inputUser = new User();
        inputUser.setUserId(1L);
        inputUser.setUsername("existingUser");  // Duplicate username
        inputUser.setEmail("existing@example.com");  // Duplicate email

        // Mock the necessary method calls
        when(userRepository.findByUserId(inputUser.getUserId())).thenReturn(testUser);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUser));

        // Call the method to be tested and assert that it throws an exception
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(inputUser));
    }
    @Test
    public void createUser_EmailNotCorrect() {
        User inputUser = new User();
        inputUser.setUserId(1L);
        inputUser.setUsername("testUsername");
        inputUser.setEmail("testName");  // email without "@"

        assertThrows(ResponseStatusException.class, () -> userService.createUser(inputUser), "Please provide a valid email address!");
    }
}