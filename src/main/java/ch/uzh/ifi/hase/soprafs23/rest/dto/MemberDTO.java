package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDTO {
    private Long memberId;
    private Long lobbyId;
    private Long userId;
    private String email;
    private String username;
    private UserStatus status;
    private LocalDate creationDate;
    private LocalDate birthdate;
    private List<String> selectedSports;
    private List<LobbyLocationDTO> selectedLocations;
    private List<LocalDateTime> selectedDates;
    private LobbyLocationDTO suggestedLocation;
    private boolean hasLockedSelections;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getSelectedSports() {
        return selectedSports;
    }

    public void setSelectedSports(List<String> selectedSports) {
        this.selectedSports = selectedSports;
    }

    public List<LobbyLocationDTO> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<LobbyLocationDTO> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    public List<LocalDateTime> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(List<LocalDateTime> selectedDates) {
        this.selectedDates = selectedDates;
    }

    public LobbyLocationDTO getSuggestedLocation() {
        return suggestedLocation;
    }

    public void setSuggestedLocation(LobbyLocationDTO suggestedLocation) {
        this.suggestedLocation = suggestedLocation;
    }

    public boolean getHasLockedSelections() {
        return hasLockedSelections;
    }

    public void setHasLockedSelections(boolean hasLockedSelections) {
        this.hasLockedSelections = hasLockedSelections;
    }
}
