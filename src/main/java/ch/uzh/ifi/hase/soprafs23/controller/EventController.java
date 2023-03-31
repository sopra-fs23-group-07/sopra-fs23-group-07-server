package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.rest.dto.EventGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.EventPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<EventGetDTO> getAllEvents() {
        // fetch all events in the internal representation
        List<Event> events = eventService.getEvents();
        List<EventGetDTO> eventGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Event event : events) {
            eventGetDTOs.add(DTOMapper.INSTANCE.convertEntityToEventGetDTO(event));
        }
        return eventGetDTOs;
    }

    //create Event
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public EventGetDTO createEvent(@RequestBody EventPostDTO eventPostDTO) {
        // convert API event to internal representation
        Event eventInput = DTOMapper.INSTANCE.convertEventPostDTOtoEntity(eventPostDTO);

        // create event
        Event createdEvent = eventService.createEvent(eventInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToEventGetDTO(createdEvent);
    }

    //for accessing specific event
    @GetMapping ("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EventGetDTO getEvent(@PathVariable Long eventId) {
        // fetch event
        Event foundEvent= eventService.getEvent(eventId);
        //converting internal representation to api representation
        return DTOMapper.INSTANCE.convertEntityToEventGetDTO(foundEvent);}


    @PutMapping("/{eventId}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinEvent(@RequestBody UserPutDTO userPutDTO, @PathVariable Long eventId){

        eventService.addUser(eventId, userPutDTO.getUserId());
    }

    @PutMapping("/{eventId}/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveEvent(@RequestBody Long userId, @PathVariable Long eventId){

        eventService.removeUser(eventId, userId);
    }
    @DeleteMapping("/{eventId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteEvent(@PathVariable Long eventId){

        eventService.deleteEvent(eventId);
    }
}

