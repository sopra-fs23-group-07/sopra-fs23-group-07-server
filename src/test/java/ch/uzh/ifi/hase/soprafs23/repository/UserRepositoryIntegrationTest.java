package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByName_success() {
    // given
    User user = new User();
    user.setEmail("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.ONLINE);
    user.setToken("1");

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUsername(user.getUsername());

    // then
    assertNotNull(found.getUserId());
    assertEquals(found.getEmail(), user.getEmail());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
  }
    @Test
    public void findById_success() {
        // given
        User user = new User();
        user.setEmail("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findById(user.getUserId());

        // then
        found.ifPresent(value -> assertNotNull(value.getUserId()));
        found.ifPresent(value -> assertEquals(value.getEmail(), user.getEmail()));
        found.ifPresent(value -> assertEquals(value.getUsername(), user.getUsername()));
        found.ifPresent(value -> assertEquals(value.getToken(), user.getToken()));
        found.ifPresent(value -> assertEquals(value.getStatus(), user.getStatus()));
    }
}
