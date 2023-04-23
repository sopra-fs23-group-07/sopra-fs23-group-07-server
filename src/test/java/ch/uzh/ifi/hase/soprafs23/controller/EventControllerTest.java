package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.Participant;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EventControllerTest
 * This is a WebMvcTest which allows to test the EventController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the EventController works.
 */
@WebMvcTest(EventController.class)
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;
    private ObjectMapper objectMapper;
    private Event testEvent;
    private User testUser;
    @BeforeEach
    public void setup() {
        // given
        Location location = new Location();
        location.setAddress("Seestrasse 1, 8000 Zürich");
        location.setLatitude(37.782118);
        location.setLongitude(-122.420016);
        testUser = new User();
        testUser.setUserId(1L);
        testEvent = new Event();
        testEvent.setEventId(1L);
        testEvent.setEventName("Test Event");
        testEvent.setEventLocation(location);
        testEvent.setEventDate(LocalDateTime.parse("2023-05-05T18:00:00"));
        testEvent.setEventSport("Soccer");
        testEvent.setEventRegion("ZH");
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenEvents_whenGetEvents_thenReturnJsonArray() throws Exception {

        List<Event> allEvents = Collections.singletonList(testEvent);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(eventService.getEvents()).willReturn(allEvents);

        // when
        MockHttpServletRequestBuilder getRequest = get("/events").contentType(MediaType.APPLICATION_JSON);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is(testEvent.getEventName())))
                .andExpect(jsonPath("$[0].eventLocationDTO.address", is(testEvent.getEventLocation().getAddress())))
                .andExpect(jsonPath("$[0].eventLocationDTO.latitude", is(testEvent.getEventLocation().getLatitude())))
                .andExpect(jsonPath("$[0].eventLocationDTO.longitude", is(testEvent.getEventLocation().getLongitude())))
                .andExpect(jsonPath("$[0].eventDate", is(testEvent.getEventDate().format(formatter))))
                .andExpect(jsonPath("$[0].eventSport", is(testEvent.getEventSport())))
                .andExpect(jsonPath("$[0].eventRegion", is(testEvent.getEventRegion())));
    }
    @Test
    void testCreateEvent() throws Exception {
        // given
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setAddress("Seestrasse 1, 8000 Zurich");
        locationDTO.setLongitude(37.782118);
        locationDTO.setLatitude(-122.420016);
        EventPostDTO eventPostDTO = new EventPostDTO();
        eventPostDTO.setEventName("Test Event");
        eventPostDTO.setEventSport("Soccer");
        eventPostDTO.setEventRegion("ZH");
        eventPostDTO.setEventDate("2023-05-05T18:00:00");
        eventPostDTO.setEventLocationDTO(locationDTO);
        eventPostDTO.setEventCreator(1L);

        Location location = new Location();
        location.setAddress("Seestrasse 1, 8000 Zurich");
        location.setLongitude(37.782118);
        location.setLatitude(-122.420016);
        Participant participant = new Participant();
        participant.setUser(testUser);
        Event createdEvent = new Event();
        createdEvent.setEventName("Test Event");
        createdEvent.setEventSport("Soccer");
        createdEvent.setEventRegion("ZH");
        createdEvent.setEventDate(LocalDateTime.parse("2023-05-05T18:00:00"));
        createdEvent.setEventLocation(location);
        createdEvent.addEventParticipant(participant);
        EventGetDTO expectedEventGetDTO = DTOMapper.INSTANCE.convertEntityToEventGetDTO(createdEvent);

        given(eventService.createEvent(any(Event.class))).willAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setEventId(1L);
            return event;
        });

        // when
        MvcResult mvcResult = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPostDTO)))
                        .andExpect(status().isCreated())
                        .andReturn();

        // then
        verify(eventService).createEvent(any(Event.class));
        verify(eventService).addParticipant(1L, 1L);
        EventGetDTO actualEventGetDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventGetDTO.class);
        assertEquals(expectedEventGetDTO.getEventName(), actualEventGetDTO.getEventName());
        assertEquals(expectedEventGetDTO.getEventSport(), actualEventGetDTO.getEventSport());
        assertEquals(expectedEventGetDTO.getEventRegion(), actualEventGetDTO.getEventRegion());
        assertEquals(expectedEventGetDTO.getEventDate(), actualEventGetDTO.getEventDate());
        assertEquals(expectedEventGetDTO.getEventLocationDTO().getAddress(), actualEventGetDTO.getEventLocationDTO().getAddress());
        assertEquals(expectedEventGetDTO.getEventLocationDTO().getLongitude(), actualEventGetDTO.getEventLocationDTO().getLongitude());
        assertEquals(expectedEventGetDTO.getEventLocationDTO().getLatitude(), actualEventGetDTO.getEventLocationDTO().getLatitude());
    }
    @Test
    public void givenEventId_whenGetEvent_thenReturnJson() throws Exception {

        given(eventService.getEvent(testEvent.getEventId())).willReturn(testEvent);

        MockHttpServletRequestBuilder getRequest = get("/events/" + testEvent.getEventId()).contentType(MediaType.APPLICATION_JSON);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(testEvent.getEventId().intValue())))
                .andExpect(jsonPath("$.eventName", is(testEvent.getEventName())))
                .andExpect(jsonPath("$.eventLocationDTO.address", is(testEvent.getEventLocation().getAddress())))
                .andExpect(jsonPath("$.eventLocationDTO.latitude", is(testEvent.getEventLocation().getLatitude())))
                .andExpect(jsonPath("$.eventLocationDTO.longitude", is(testEvent.getEventLocation().getLongitude())))
                .andExpect(jsonPath("$.eventDate", is(testEvent.getEventDate().format(formatter))))
                .andExpect(jsonPath("$.eventSport", is(testEvent.getEventSport())))
                .andExpect(jsonPath("$.eventRegion", is(testEvent.getEventRegion())));
    }
    @Test
    public void givenUserAndEvent_whenJoinEvent_thenAddUserToParticipants() throws Exception {

        UserEventDTO testUserEventDTO = new UserEventDTO();
        testUserEventDTO.setUserId(testUser.getUserId());

        when(eventService.getEvent(anyLong())).thenReturn(testEvent);
        doNothing().when(eventService).addParticipant(anyLong(), anyLong());

        mockMvc.perform(put("/events/{eventId}/join", testEvent.getEventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUserEventDTO)))
                        .andExpect(status().isNoContent());

        verify(eventService).addParticipant(testEvent.getEventId(), testUser.getUserId());
    }
    @Test
    public void givenEventAndUser_whenLeaveEvent_thenReturnStatusOk() throws Exception {

        UserEventDTO testUserEventDTO = new UserEventDTO();
        testUserEventDTO.setUserId(testUser.getUserId());

        given(eventService.getEvent(testEvent.getEventId())).willReturn(testEvent);

        // Act
        mockMvc.perform(put("/events/{eventId}/leave", testEvent.getEventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUserEventDTO)))
                        .andExpect(status().isOk());

        // Assert
        verify(eventService, times(1)).removeParticipant(testEvent.getEventId(), testUser.getUserId());
    }
    @Test
    public void givenValidEventId_whenDeleteEvent_thenStatusOk() throws Exception {

        // when
        MockHttpServletRequestBuilder deleteRequest = delete("/events/" + testEvent.getEventId() + "/delete")
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(deleteRequest).andExpect(status().isOk());
        verify(eventService, times(1)).deleteEvent(testEvent.getEventId());
    }






    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
