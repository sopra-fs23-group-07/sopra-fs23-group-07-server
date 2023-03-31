package ch.uzh.ifi.hase.soprafs23.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal Event Representation
 * This class composes the internal representation of the event and defines how
 * events are stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "EVENT")
public class Event implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq", sequenceName = "event_sequence", initialValue = 1)
    private Long eventId;

    @Column(nullable = true)
    private String eventName;

    @Column(nullable = true, unique = false)
    private String eventLocation;

    @Column(nullable = true, unique = false)
    private String eventSport;

    @Column(nullable = true, unique = true)
    private String eventRegion;

    @Column(nullable = true)
    private Integer eventMaxParticipants;

    @Column(nullable = true, unique = false, updatable = false)
    private LocalDate eventDate;

    @ManyToMany(mappedBy = "events")
    @JsonIgnoreProperties("events")
    private List<User> eventParticipants = new ArrayList<>();


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

    public String getEventRegion() {
        return eventRegion;
    }

    public void setEventRegion(String eventRegion) {
        this.eventRegion = eventRegion;
    }

    public Integer getEventMaxParticipants() {
        return eventMaxParticipants;
    }
    public void setEventMaxParticipants(Integer eventMaxParticipants) {
        this.eventMaxParticipants = eventMaxParticipants;
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

    public void setEventParticipants(List<User> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public List<User> getEventParticipants(){
        return eventParticipants;
    }

    public void addEventParticipant(User userToAdd) {
        eventParticipants.add(userToAdd);
    }

    public void removeEventParticipant(User userToRemove) {
        eventParticipants.removeIf(user -> user.equals(userToRemove));
    }

    public boolean eventIsFull() {
        return eventParticipants.size() == eventMaxParticipants;
    }
    public boolean eventIsEmpty() {
        return eventParticipants.size() == 0;
    }

}
