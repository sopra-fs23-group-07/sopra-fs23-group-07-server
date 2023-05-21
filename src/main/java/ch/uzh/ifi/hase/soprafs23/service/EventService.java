package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.entity.Participant;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.EventRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ParticipantRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    private final UserUtil userUtil;

    @Autowired
    public EventService(@Qualifier("eventRepository") EventRepository eventRepository,
                        @Qualifier("userRepository") UserRepository userRepository,
                        @Qualifier("participantRepository") ParticipantRepository participantRepository,
                        UserUtil userUtil){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.userUtil = userUtil;
    }

    public List<Event> getEvents() {
        return this.eventRepository.findAll();
    }
    @Transactional
    public void updateIsNewEvent(Long eventId, boolean isNewEvent) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setIsNewEvent(isNewEvent);
            eventRepository.save(event);
        }
    }
    public Event createEvent(Event newEvent) {
        if (newEvent.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose event date in the future.");
        }
        if (newEvent.getEventMaxParticipants() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum number of event participants cannot exceed 30 people.");
        }
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newEvent.setIsNewEvent(true);
        newEvent = eventRepository.save(newEvent);
        newEvent.getEventLocation().setEventId(newEvent.getEventId());
        eventRepository.flush();

        // create a ScheduledExecutorService with one thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Copy eventId into a final variable so it can be accessed within the lambda expression
        final Long eventId = newEvent.getEventId();

        // schedule the task to run after 5 seconds
        scheduler.schedule(() -> {
            // this block of code will be executed after 5 seconds
            updateIsNewEvent(eventId, false);
            scheduler.shutdown();  // shut down the executor service after the task has run
        }, 5, TimeUnit.SECONDS);


        log.debug("Created Information for Event: {}", newEvent);
        return newEvent;
    }

    public Event getEvent(long eventId) {
        Event eventToFind = eventRepository.findByEventId(eventId);
        if (eventToFind == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "eventId", "was"));
        }
        return eventToFind;
    }

    public Participant getParticipant(Event event, User user) {
        Optional<Participant> participantToFind = participantRepository.findByEventAndUser(event, user);
        if (participantToFind.isEmpty()) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "participantId", "was"));
        }
        return participantToFind.get();
    }

    public void addParticipant(long eventId, long userId, String token) {
        Event event = getEvent(eventId);
        checkIfEventIsOver(event);
        User databaseUser = userUtil.getUser(userId, token);
        if (event.eventIsFull()) {
            String baseErrorMessage = "The %s provided %s full";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "Event", "is"));
        }
        if (participantRepository.findByEventAndUser(event, databaseUser).isPresent()) {
            String baseErrorMessage = "The %s provided %s already participant of this event";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "userId", "is"));
        }
        checkIfUserIsParticipantOfEvent(event, databaseUser);
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

    public void removeParticipant(long eventId, long userId, String token) {
        Event event = getEvent(eventId);
        checkIfEventIsOver(event);
        User databaseUser = userUtil.getUser(userId, token);
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
    private void checkIfUserIsParticipantOfEvent(Event event, User user) {
        if (user.getEvents().contains(event)) {
            String baseErrorMessage = "The %s provided %s already participant of this event.";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "User", "is"));
        }
    }
    private void checkIfEventIsOver(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is over, you cannot join anymore");
        }
    }
}