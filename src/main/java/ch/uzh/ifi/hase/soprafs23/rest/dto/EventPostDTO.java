package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.time.LocalDate;
import java.util.List;

public class EventPostDTO {
    private Long eventId;
    private String eventName;
    private String eventLocation;
    private LocalDate eventDate;
    private String eventSport;
    private String eventRegion;
    private Integer eventMaxParticipants;
    private List<User> eventParticipants;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventSport() {
        return eventSport;
    }
    public void setEventSport(String eventSport) {
        this.eventSport = eventSport;
    }
    public String getEventRegion() {
        return eventRegion;
    }
    public void setEventRegion(String eventRegion) {
        this.eventRegion = eventRegion;
    }

    public List<User> getEventParticipants(){
        return eventParticipants;
    }

    public void setEventParticipants(List<User> eventParticipants){
        this.eventParticipants = eventParticipants;
    }

    public Integer getEventMaxParticipants() {
        return eventMaxParticipants;
    }
    public void setEventMaxParticipants(Integer eventMaxParticipants) {
        this.eventMaxParticipants = eventMaxParticipants;
    }
}

