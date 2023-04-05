package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.entity.Participant;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("participantRepository")
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findById(long participantId);
    Optional<Participant> findByEventAndUser(Event event, User user);

    List<Participant> findByUser(User user);
    List<Participant> findByEvent(Event event);
}

