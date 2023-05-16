package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(LocalDate.now());

        checkIfUserExists(newUser, newUser);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated, User userBeingUpdated) {
        String username = userToBeCreated.getUsername();
        String email = userToBeCreated.getEmail();

        if (email != null && (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide a valid email address!");

        }

        User duplicateUser = userRepository.findAll().stream()
                .filter(user -> user != userBeingUpdated &&
                        (user.getUsername().equalsIgnoreCase(username) || user.getEmail().equalsIgnoreCase(email)))
                .findFirst()
                .orElse(null);

        if (duplicateUser != null) {
            String conflictType = (duplicateUser.getUsername().equalsIgnoreCase(username)) ? "username" : "email address";
            String operation = (userToBeCreated.equals(userBeingUpdated)) ? "created" : "updated";
            String errorMessage = "The %s provided %s not unique. Therefore, the user could not be %s!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(errorMessage, conflictType, "is", operation));
        }
    }


    public User loginUser(User userToBeLoggedIn) {
        User userInDb = userRepository.findByUsername(userToBeLoggedIn.getUsername());
        if (userInDb == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "username", "was"));
        }
        if (!userInDb.getPassword().equals(userToBeLoggedIn.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
        if (userInDb.getStatus().equals(UserStatus.ONLINE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already logged in, please logout first");
        }
        userInDb.setStatus(UserStatus.ONLINE);
        userInDb.setToken(UUID.randomUUID().toString());

        log.debug("Created Information for User: {}", userToBeLoggedIn);

        return userInDb;
    }
    public void logoutUser(Long userId) {
        User userToBeLoggedOut = retrieveUser(userId);
        userToBeLoggedOut.setStatus(UserStatus.OFFLINE);
    }
    public User retrieveUser(Long userId) {
        User userToFind = userRepository.findByUserId(userId);
        if (userToFind == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "userId", "was"));
        }
        return userToFind;
    }

    public User getUser(long userId, String token) {
        User userToFind = retrieveUser(userId);
        if (!userToFind.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "userToken is not valid");
        }
        return userToFind;
    }
    public void updateUser(User inputUser) {
        User databaseUser = getUser(inputUser.getUserId(), inputUser.getToken());

        // Check if username and email address already exist
        if ((!Objects.equals(inputUser.getUsername(), databaseUser.getUsername())) ||
                (!Objects.equals(inputUser.getEmail(), databaseUser.getEmail()))) {
            checkIfUserExists(inputUser, databaseUser);
        }

        // Copy non-null properties from inputUser to databaseUser
        BeanUtils.copyProperties(inputUser, databaseUser, getNullPropertyNames(inputUser));

        // Save the merged user object
        userRepository.save(databaseUser);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
