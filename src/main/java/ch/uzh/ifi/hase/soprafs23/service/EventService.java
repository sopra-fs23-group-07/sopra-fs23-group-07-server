package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.entity.Participant;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.EventRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LocationRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ParticipantRepository;
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


/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public EventService(@Qualifier("eventRepository") EventRepository eventRepository,
                        @Qualifier("userRepository") UserRepository userRepository,
                        @Qualifier("participantRepository") ParticipantRepository participantRepository,
                        LocationRepository locationRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.locationRepository = locationRepository;
    }

    public List<Event> getEvents() {
        return this.eventRepository.findAll();
    }

    public Event createEvent(Event newEvent) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newEvent = eventRepository.save(newEvent);
        newEvent.getEventLocation().setEventId(newEvent.getEventId());
        eventRepository.flush();

        log.debug("Created Information for Event: {}", newEvent);
        return newEvent;
    }


    public Event getEvent(long eventId) {
        Optional<Event> eventToFind = eventRepository.findById(eventId);
        if (eventToFind.isEmpty()) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "eventId", "was"));
        }
        return eventToFind.get();
    }
    public User getUser(long userId) {
        User userToFind = userRepository.findByUserId(userId);
        if (userToFind == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "userId", "was"));
        }
        return userToFind;
    }
    public Participant getParticipant(Event event, User user) {
        Optional<Participant> participantToFind = participantRepository.findByEventAndUser(event, user);
        if (participantToFind.isEmpty()) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "participantId", "was"));
        }
        return participantToFind.get();
    }

    public void addParticipant(long eventId, long userId) {
        Event event = getEvent(eventId);
        User databaseUser = getUser(userId);
        Participant participant = new Participant();
        participant.setUser(databaseUser);
        participant.setEventId(event.getEventId());

        event.addEventParticipant(participant);
        event.addEventUser(databaseUser);
        databaseUser.addEvent(event);
        participantRepository.save(participant);
        userRepository.save(databaseUser);
        eventRepository.save(event);
    }

    public void removeParticipant(long eventId, long userId) {
        Event event = getEvent(eventId);
        User databaseUser = getUser(userId);
        Participant participant = getParticipant(event, databaseUser);

        event.removeEventParticipant(participant);
        event.removeEventUser(databaseUser);
        databaseUser.removeEvent(event);
        eventRepository.save(event);
        userRepository.save(databaseUser);
        participantRepository.delete(participant);
    }
    public void deleteEvent(long eventId) {
        Event event = getEvent(eventId);
        eventRepository.delete(event);
    }

    public List<Participant> getParticipants(){
        return this.participantRepository.findAll();
    }
}