package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Column(nullable = true, unique = true)
    private String eventName;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Location eventLocation;

    @Column(nullable = true, unique = false)
    private String eventSport;

    @Column(nullable = true, unique = false)
    private String eventRegion;

    @Column(nullable = true)
    private Integer eventMaxParticipants;

    @Column(nullable = true, unique = false, updatable = false)
    private LocalDateTime eventDate;

    @Column
    private boolean isNewEvent = true;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> eventParticipants = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private List<User> eventUsers = new ArrayList<>();

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

    public Location getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(Location eventLocation) {
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

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventSport() {
        return eventSport;
    }

    public void setEventSport(String eventSport) {
        this.eventSport = eventSport;
    }

    public void setEventParticipants(List<Participant> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public List<Participant> getEventParticipants(){
        return eventParticipants;
    }

    public void addEventParticipant(Participant participantToAdd) {
        eventParticipants.add(participantToAdd);
    }

    public void removeEventParticipant(Participant participantToRemove) {
        eventParticipants.removeIf(participant -> participant.equals(participantToRemove));
    }

    public boolean eventIsFull() {
        return eventParticipants.size() == eventMaxParticipants;
    }
    public boolean eventIsEmpty() {
        return eventParticipants.isEmpty();
    }

    public Integer getEventParticipantsCount() {return eventParticipants.size();}

    public void addEventUser(User userToAdd) {
        eventUsers.add(userToAdd);
    }
    public void removeEventUser(User userToRemove) {
        eventUsers.removeIf(user -> user.equals(userToRemove));
    }

    public boolean getIsNewEvent() {
        return isNewEvent;
    }

    public void setIsNewEvent(boolean newEvent) {
        isNewEvent = newEvent;
    }
}

