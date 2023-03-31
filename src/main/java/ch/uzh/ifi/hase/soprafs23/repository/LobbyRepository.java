package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface LobbyRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  Optional<User> findById(Long userId);

}
