package ch.uzh.ifi.hase.soprafs23.util;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserUtil {
    private final UserRepository userRepository;

    public UserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getUser(Long userId, String token) {
        User userToFind = userRepository.findByUserId(userId);
        if (userToFind == null) {
            String baseErrorMessage = "Please login or register";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "userId", "was"));
        }
        if (!userToFind.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "userToken is not valid");
        }
        return userToFind;
    }
}

