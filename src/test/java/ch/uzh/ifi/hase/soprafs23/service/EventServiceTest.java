package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.Participant;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.EventRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ParticipantRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        Location location = new Location();
        location.setAddress("Seestrasse 1, 8000 Zürich");
        location.setLatitude(37.782118);
        location.setLongitude(-122.420016);
        location.setLocationId(1L);
        location.setEventId(1L);
        testEvent = new Event();
        testEvent.setEventId(1L);
        testEvent.setEventName("TestEvent");
        testEvent.setEventLocation(location);
        testEvent.setEventDate(LocalDateTime.parse("2023-05-05T18:00"));
        testEvent.setEventSport("Soccer");
        testEvent.setEventRegion("ZH");
        testEvent.setEventMaxParticipants(10);
        testUser = new User();
        testUser.setUserId(1L);
        // when -> any object is being saved in the eventRepository -> return the dummy
        // testEvent
        when(eventRepository.save(Mockito.any())).thenReturn(testEvent);
        when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }
    @Test
    void getEvents_ShouldReturnListOfEvents() {
        // Create some test events
        Event testEvent2 = new Event();
        testEvent2.setEventId(2L);
        List<Event> testEvents = Arrays.asList(testEvent, testEvent2);

        // Mock the repository method call to return the test events
        when(eventRepository.findAll()).thenReturn(testEvents);

        // Call the method to be tested
        List<Event> returnedEvents = eventService.getEvents();

        // Verify that the returned events match the test events
        assertEquals(testEvents, returnedEvents);
    }

    @Test
    void createEvent_validInputs_success() {
        // when -> any object is being saved in the eventRepository -> return the dummy
        // testEvent
        Event createdEvent = eventService.createEvent(testEvent);

        // then
        verify(eventRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testEvent.getEventId(), createdEvent.getEventId());
        assertEquals(testEvent.getEventName(), createdEvent.getEventName());
        assertEquals(testEvent.getEventLocation(), createdEvent.getEventLocation());
        assertEquals(testEvent.getEventDate(), createdEvent.getEventDate());
        assertEquals(testEvent.getEventSport(), createdEvent.getEventSport());
        assertEquals(testEvent.getEventRegion(), createdEvent.getEventRegion());
    }
    @Test
    void getEvent_eventFound() {
        // Set up mock repository to return the test Event object when finding event by ID
        when(eventRepository.findByEventId(1L)).thenReturn(Optional.of(testEvent));

        // Call the method under test and verify that the returned Event object matches the test Event object
        Event foundEvent = eventService.getEvent(1L);
        assertEquals(foundEvent.getEventId(), testEvent.getEventId());
        assertEquals(foundEvent.getEventName(), testEvent.getEventName());

        // Verify that the repository was called with the correct ID
        verify(eventRepository, Mockito.times(1)).findByEventId(1L);
    }
    @Test
    void getEvent_eventNotFound() {
        // Set up mock repository to return null when finding event by ID
        when(eventRepository.findByEventId(1L)).thenReturn(Optional.empty());

        // Call the method under test and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> eventService.getEvent(1L));

        // Verify that the repository was called with the correct ID
        verify(eventRepository, Mockito.times(1)).findByEventId(1L);
    }
    @Test
    void getUser_userFound() {
        // Set up mock repository to return a User with ID 1 when finding by ID
        User mockUser = new User();
        mockUser.setUserId(1L);
        when(userRepository.findByUserId(1L)).thenReturn(mockUser);

        // Call the method under test
        User result = eventService.getUser(1L);

        // Verify that the repository was called with the correct ID
        verify(userRepository, Mockito.times(1)).findByUserId(1L);

        // Verify that the result matches the mock User
        assertEquals(mockUser, result);
    }

    @Test
    void getUser_userNotFound() {
        // Set up mock repository to return null when finding a User by ID
        when(userRepository.findByUserId(1L)).thenReturn(null);

        // Call the method under test and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> eventService.getUser(1L));

        // Verify that the repository was called with the correct ID
        verify(userRepository, Mockito.times(1)).findByUserId(1L);
    }

    @Test
    void getParticipant_participantFound() {

        User user = new User();
        user.setUsername("testuser");

        // create a test participant and add it to the event
        Participant participant = new Participant();
        participant.setEventId(testEvent.getEventId());
        participant.setUser(user);
        participantRepository.save(participant);

        // Set up mock repository to return the participant when finding by event and user
        when(participantRepository.findByEventAndUser(testEvent, user)).thenReturn(Optional.of(participant));

        // Call the method under test
        Participant foundParticipant = eventService.getParticipant(testEvent, user);

        // Verify that the repository was called with the correct event and user
        verify(participantRepository, Mockito.times(1)).findByEventAndUser(testEvent, user);

        // Check that the correct participant was returned
        assertEquals(foundParticipant.getParticipantId(), participant.getParticipantId());
        assertEquals(foundParticipant.getEventId(), testEvent.getEventId());
        assertEquals(foundParticipant.getUserId(), user.getUserId());
    }
    @Test
    void getParticipant_participantNotFound() {

        User user = new User();
        user.setUserId(1L);

        // Set up the participant repository to return null when finding a participant by event and user
        when(participantRepository.findByEventAndUser(testEvent, user)).thenReturn(Optional.empty());

        // Call the method under test and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> eventService.getParticipant(testEvent, user));

        // Verify that the repository was called with the correct event and user
        verify(participantRepository, Mockito.times(1)).findByEventAndUser(testEvent, user);
    }
    @Test
    void addParticipant_ShouldAddParticipantToEventAndUser() {

        // Mock the necessary method calls
        when(eventRepository.findByEventId(testEvent.getEventId())).thenReturn(java.util.Optional.of(testEvent));
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // Call the method to be tested
        eventService.addParticipant(testEvent.getEventId(), testUser.getUserId());

        // Verify that the expected changes were made to the event object
        assertEquals(1, testEvent.getEventParticipants().size());

        // Capture the argument passed to participantRepository.save()
        ArgumentCaptor<Participant> participantCaptor = ArgumentCaptor.forClass(Participant.class);
        verify(participantRepository).save(participantCaptor.capture());

        // Assert that the saved participant is added to the event's participant set
        assertTrue(testEvent.getEventParticipants().contains(participantCaptor.getValue()));

        // Verify that the expected changes were made to the user object
        assertEquals(1, testUser.getEvents().size());
        assertTrue(testUser.getEvents().contains(testEvent));

        // Verify that the corresponding save methods were called
        verify(participantRepository, times(1)).save(any(Participant.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }
    @Test
    void addParticipant_eventIsFull_throwsException() {
        when(eventRepository.findByEventId(testEvent.getEventId())).thenReturn(Optional.of(testEvent));
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);
        testEvent.setEventMaxParticipants(1);
        eventService.addParticipant(testEvent.getEventId(), testUser.getUserId());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                eventService.addParticipant(testEvent.getEventId(), testUser.getUserId()));

        // Verify that the exception message contains the expected error message
        String expectedErrorMessage = "The Event provided is full";
        assertTrue(Objects.requireNonNull(exception.getReason()).contains(expectedErrorMessage));

        // Verify that the participant was not added to the event
        assertEquals(1, testEvent.getEventParticipants().size());
    }


    @Test
    void removeParticipant_ShouldRemoveParticipantFromEventAndUser() {
        // Mock the necessary method calls
        when(eventRepository.findByEventId(testEvent.getEventId())).thenReturn(java.util.Optional.of(testEvent));
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // Create a participant and add it to the testEvent's set of participants
        Participant testParticipant = new Participant();
        testParticipant.setUser(testUser);
        testParticipant.setEventId(testEvent.getEventId());
        testEvent.addEventParticipant(testParticipant);

        // Mock the behavior of the participantRepository's findByUserAndEvent method
        when(participantRepository.findByEventAndUser(testEvent, testUser)).thenReturn(Optional.of(testParticipant));

        // Call the method to be tested
        eventService.removeParticipant(testEvent.getEventId(), testUser.getUserId());

        // Verify that the expected changes were made to the event object
        assertEquals(0, testEvent.getEventParticipants().size());
    }
    @Test
    void deleteEvent_ShouldDeleteEvent() {
        // Mock the necessary method calls
        when(eventRepository.findByEventId(testEvent.getEventId())).thenReturn(java.util.Optional.of(testEvent));

        // Call the method to be tested
        eventService.deleteEvent(testEvent.getEventId());

        // Verify that the event was deleted
        verify(eventRepository, times(1)).delete(testEvent);
    }
    @Test
    void moreThan30Members() {
        Event event = new Event();
        event.setEventMaxParticipants(31);
        assertThrows(ResponseStatusException.class, () -> eventService.createEvent(event));
    }
    @Test
    void addParticipant_userIsAlreadyParticipant_throwsException() {
        // Mock the behavior of the eventRepository
        when(eventRepository.findByEventId(testEvent.getEventId())).thenReturn(Optional.of(testEvent));

        // Mock the behavior of the userRepository
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // Create an event and add the user as a participant
        Event createdEvent = eventService.createEvent(testEvent);
        eventService.addParticipant(createdEvent.getEventId(), testUser.getUserId());

        // Call the method to add the participant again and expect an exception to be thrown
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                eventService.addParticipant(createdEvent.getEventId(), testUser.getUserId()));

        // Verify that the exception message contains the expected error message
        String expectedErrorMessage = "The User provided is already participant of this event.";
        assertTrue(Objects.requireNonNull(exception.getReason()).contains(expectedErrorMessage));
    }
}