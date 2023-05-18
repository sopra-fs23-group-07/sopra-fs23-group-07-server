package ch.uzh.ifi.hase.soprafs23.util;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class UserUtilTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUtil userUtil;

    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setEmail("test@Name");
        testUser.setUsername("testUsername");
        testUser.setPassword("123");
        testUser.setToken("token");
        testUser.setStatus(UserStatus.OFFLINE);

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        when(userRepository.save(any())).thenReturn(testUser);
    }
    @Test
    void getUser_ShouldThrowExceptionWhenUnknownUser() {

        // Mock the UserRepository to return a 404 NOT FOUND response when getUser is called with an unknown user ID
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Long userId = testUser.getUserId();
        String userToken = testUser.getToken();

        // Call the method to be tested
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userUtil.getUser(userId, userToken));

        // Verify that the exception has a 404 NOT FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    void getUser_ShouldReturnUserById() {

        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // Call the method to be tested
        User retrievedUser = userUtil.getUser(testUser.getUserId(), testUser.getToken());

        // Assert that the captured user ID matches the ID of the test user
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());
        // Assert that the returned user is the same as the test user
        assertEquals(testUser, retrievedUser);
    }
    @Test
    void getUser_UserNotFound() {

        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(null);

        Long userId = testUser.getUserId();
        String userToken = testUser.getToken();

        // Call the method to be tested and assert that it throws the expected exception
        assertThrows(ResponseStatusException.class, () -> userUtil.getUser(userId, userToken));
    }
}
