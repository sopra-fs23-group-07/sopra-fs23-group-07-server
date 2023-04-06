package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import org.hibernate.annotations.Type;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobbyId", insertable = false, updatable = false)
    private Lobby lobby;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true, unique = true)
    private String username;

    @Column(nullable = true)
    private UserStatus status;

    @Column(nullable = true, unique = false, updatable = false)
    private LocalDate creationDate;
    @Column
    private LocalDate birthdate;
    @ElementCollection
    @CollectionTable(name = "member_sport", joinColumns = @JoinColumn(name = "member_id"))
    private List<String> selectedSports;
    @ElementCollection
    @CollectionTable(name = "member_location", joinColumns = @JoinColumn(name = "member_id"))
    private List<Location> selectedLocations;
    @ElementCollection
    @CollectionTable(name = "member_dates", joinColumns = @JoinColumn(name = "member_id"))
    private List<LocalDateTime> selectedDates;

    @Column
    private String suggestedLocation;
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

    public Long getParticipantId() {
        return memberId;
    }

    public void setParticipantId(Long participantId) {
        this.memberId = participantId;
    }

    public List<String> getSelectedSports() {
        return selectedSports;
    }

    public void setSelectedSports(List<String> selectedSports) {
        this.selectedSports = selectedSports;
    }

    public List<Location> getSelectedLocations() {
        return selectedLocations;
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

    public String getSuggestedLocation() {
        return suggestedLocation;
    }

    public void setSuggestedLocation(String suggestedLocation) {
        this.suggestedLocation = suggestedLocation;
    }

    public boolean isHasLockedSelections() {
        return hasLockedSelections;
    }

    public void setHasLockedSelections(boolean hasLockedSelections) {
        this.hasLockedSelections = hasLockedSelections;
    }
}
