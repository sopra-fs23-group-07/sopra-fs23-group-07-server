package ch.uzh.ifi.hase.soprafs23.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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
    @ElementCollection
    @CollectionTable(name = "location_votes", joinColumns = @JoinColumn(name = "location_id"))
    private Set<Long> memberVotes = new HashSet<>();
    @Column(nullable = true)
    private Long lobbyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobbyId", insertable = false, updatable = false)
    private Lobby lobby;
    @Column(nullable = false)
    private String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
