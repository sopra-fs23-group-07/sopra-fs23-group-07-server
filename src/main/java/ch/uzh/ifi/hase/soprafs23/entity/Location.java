package ch.uzh.ifi.hase.soprafs23.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "LOCATION")
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq", sequenceName = "location_sequence", initialValue = 1)
    private Long locationId;
    @Column(nullable = true)
    private Long memberId;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private double latitude;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "location_votes", joinColumns = @JoinColumn(name = "location_id"))
    private Set<Long> memberVotes = new HashSet<>();
    @Column(nullable = true)
    private Long lobbyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobbyId", insertable = false, updatable = false)
    private Lobby lobby;
    @Column(nullable = true)
    private String address;
    @Column(nullable = true)
    private Long eventId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private List<Member> selectedMembers;
    @OneToOne
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member suggestedBy;
    @Column(nullable = true)
    private String locationType; // can be "DECIDED" or "OTHER"

    // Getters and setters for the longitude and latitude fields
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return longitude +","+ latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getMemberVotes() {
        return memberVotes.size();
    }

    public void addMemberVotes(Long memberId) {
        if (!memberVotes.add(memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Member with memberId "+memberId+" has already voted" +
                    " for this location");
        }
        memberVotes.add(memberId);
    }
    public void removeMemberVotes(Long memberId) {
        if (memberVotes.add(memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Member with memberId "+memberId+" has not yet voted" +
                    " for this location");
        }
        memberVotes.remove(memberId);
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setSelectedMembers(List<Member> members) {
        this.selectedMembers = members;
    }
    public List<Member> getSelectedMembers() {
        return this.selectedMembers;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
