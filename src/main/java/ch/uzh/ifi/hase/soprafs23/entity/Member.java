package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="MEMBER")
public class Member implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "member_sequence", initialValue = 1)
    private Long memberId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long lobbyId;
    @Column(nullable = true)
    private Long locationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobbyId", insertable = false, updatable = false)
    private Lobby lobby;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    /**@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId", insertable = false, updatable = false)
    private Location location;**/

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private UserStatus status;

    @Column(updatable = false)
    private LocalDate creationDate;
    @Column
    private LocalDate birthdate;
    @ElementCollection
    @CollectionTable(name = "member_sport", joinColumns = @JoinColumn(name = "member_id"))
    private List<String> selectedSports = new ArrayList<>();
    //@ElementCollection
    //@CollectionTable(name = "member_location", joinColumns = @JoinColumn(name = "member_id"))
    @ManyToMany(mappedBy = "selectedMembers", fetch = FetchType.EAGER)
    private List<Location> selectedLocations = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @CollectionTable(name = "member_dates", joinColumns = @JoinColumn(name = "member_id"))
    private List<LocalDateTime> selectedDates;

    @OneToOne(mappedBy = "suggestedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Location suggestedLocation;
    @Column
    private boolean hasLockedSelections;

    public void setUser(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.birthdate = user.getBirthdate();
        this.creationDate = user.getCreationDate();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getBirthdate(){
        return birthdate;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<String> getSelectedSports() {
        return selectedSports;
    }

    public void setSelectedSports(List<String> selectedSports) {
        this.selectedSports = selectedSports;
    }

    public void addSelectedSport(String sport) {this.selectedSports.add(sport);}

    public List<Location> getSelectedLocations() {
        return selectedLocations;
    }
    public void addSelectedLocation(Location location) {
        this.selectedLocations.add(location);
    }
    public void removeSelectedLocation(Location location) {
        selectedLocations.removeIf(location1 -> location1.equals(location));
    }

    public void setSelectedLocations(List<Location> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    public List<LocalDateTime> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(List<LocalDateTime> selectedDates) {
        this.selectedDates = selectedDates;
    }

    public Location getSuggestedLocation() {
        return suggestedLocation;
    }
    public void setSuggestedLocation(Location suggestedLocation) {
        this.suggestedLocation = suggestedLocation;
    }

    public boolean getHasLockedSelections() {
        return hasLockedSelections;
    }

    public void setHasLockedSelections(boolean bool) {
        this.hasLockedSelections = bool;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
