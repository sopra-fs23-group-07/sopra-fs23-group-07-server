package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.time.LocalDate;
import java.util.List;

public class EventGetDTO {

    private Long eventId;
    private String eventName;
    private String eventLocation;
    private String eventDate;
    private String eventSport;
    private String eventRegion;
    private Integer eventMaxParticipants;
    private List<ParticipantDTO> participantDTOs;

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

    public void setEventLocation( String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
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

    public List<ParticipantDTO> getParticipantDTOs(){
        return participantDTOs;
    }

    public void setParticipantDTOs(List<ParticipantDTO> participantDTOs){
        this.participantDTOs = participantDTOs;
    }

    public Integer getEventMaxParticipants() {
        return eventMaxParticipants;
    }
    public void setEventMaxParticipants(Integer eventMaxParticipants) {
        this.eventMaxParticipants = eventMaxParticipants;
    }
}

