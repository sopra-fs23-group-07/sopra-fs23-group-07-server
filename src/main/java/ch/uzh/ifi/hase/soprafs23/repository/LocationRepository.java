package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("locationRepository")
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(Long aLong);
}