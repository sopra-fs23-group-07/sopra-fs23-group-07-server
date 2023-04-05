package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("eventRepository")
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByEventName(String eventName);

    Optional<Event> findById(long EventId);

}

