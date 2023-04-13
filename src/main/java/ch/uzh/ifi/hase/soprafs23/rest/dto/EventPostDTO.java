package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class EventPostDTO {
    private String eventName;
    private LocationDTO eventLocationDTO;
    private String eventDate;
    private String eventSport;
    private String eventRegion;
    private Integer eventMaxParticipants;
    private Long eventCreator;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocationDTO getEventLocationDTO() {
        return eventLocationDTO;
    }

    public void setEventLocationDTO(LocationDTO eventLocationDTO) {
        this.eventLocationDTO = eventLocationDTO;
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

    public Long getEventCreator(){
        return eventCreator;
    }

    public void setEventCreator(Long eventParticipants){
        this.eventCreator = eventParticipants;
    }

    public Integer getEventMaxParticipants() {
        return eventMaxParticipants;
    }
    public void setEventMaxParticipants(Integer eventMaxParticipants) {
        this.eventMaxParticipants = eventMaxParticipants;
    }
}

