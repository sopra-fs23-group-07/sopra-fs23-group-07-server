package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
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
import java.util.Optional;
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
    newUser.setBirthdate(LocalDate.of(1900,01,01));
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
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }
  }

    private void checkIfUserExistsForLogin(User userToBeLoggedIn){
        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());
        User userByPassword = userRepository.findByPassword(userToBeLoggedIn.getPassword());

        if (userByUsername==null && userByPassword==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The username and password provided could not be found in our database. Please register to take full advantage of our awesome services!");
        }
        else if (userByUsername != userByPassword){
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT,
                    "The username and password provided do not match. Please try again with your credentials Mr. Bond.");
        }
    }

    public User loginUser(User userToBeLoggedIn){
        checkIfUserExistsForLogin(userToBeLoggedIn);

        userToBeLoggedIn.setStatus(UserStatus.ONLINE);
        userToBeLoggedIn.setToken(UUID.randomUUID().toString());

        return userToBeLoggedIn;

    }

    public void logoutUser(User userToBeLoggedOut){
      userToBeLoggedOut.setStatus(UserStatus.OFFLINE);
      userToBeLoggedOut.setToken(null);
    }

    public User getUser(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {//throw error if no user found for this id in the repository
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("user with userid %d could not be found", userId));
        }
  }
}
