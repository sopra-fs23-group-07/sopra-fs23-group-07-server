package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import java.time.LocalDate;

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
        //newUser.setBirthdate(LocalDate.of(1900, 01, 01));
        checkIfUserExists(newUser);
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
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
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
        userInDb.setStatus(UserStatus.ONLINE);
        userInDb.setToken(UUID.randomUUID().toString());
        //userRepository.save(userInDb);

        log.debug("Created Information for User: {}", userToBeLoggedIn);

        return userInDb;
    }

    public void logoutUser(long userId) {
        User userToBeLoggedOut = getUser(userId);
        userToBeLoggedOut.setStatus(UserStatus.OFFLINE);

    }
    public void userLoggedIn(String token) {
        User userByToken = userRepository.findByToken(token);
        if (userByToken == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is not valid");
        }
    }

    public User getUser(long userId) {
        User userToFind = userRepository.findByUserId(userId);
        if (userToFind == null) {
            String baseErrorMessage = "The %s provide %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "userId", "was"));
        }
        return userToFind;
    }

    public void updateUser(User inputUser) {
        User databaseUser = getUser(inputUser.getUserId());
        if (inputUser.getUsername()!=null && !inputUser.getUsername().equals("")){
            databaseUser.setUsername(inputUser.getUsername());
        }
        if (inputUser.getPassword()!=null && !inputUser.getPassword().equals("")){
            databaseUser.setPassword(inputUser.getPassword());
        }
        if (inputUser.getEmail()!=null && !inputUser.getEmail().equals("")){
            databaseUser.setEmail(inputUser.getEmail());
        }
        if (inputUser.getBirthdate()!=null && !inputUser.getBirthdate().toString().equals("")){
            databaseUser.setBirthdate(inputUser.getBirthdate());
        }
        if (inputUser.getBio()!=null && !inputUser.getBio().equals("")){
            databaseUser.setBio(inputUser.getBio());
        }
        userRepository.save(databaseUser);
    }
}
