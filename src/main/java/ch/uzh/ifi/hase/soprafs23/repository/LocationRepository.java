package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("locationRepository")
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByLocationId(long locationId);

}
