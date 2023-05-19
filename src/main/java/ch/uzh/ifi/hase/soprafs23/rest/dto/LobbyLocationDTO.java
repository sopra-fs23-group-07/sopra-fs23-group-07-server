package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Set;

public class LobbyLocationDTO {
    private Long memberId;
    private Long locationId;
    private Long lobbyId;
    private String address;
    private double longitude;
    private double latitude;
    private int memberVotes;
    private Set<Long> memberVotesIds;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public int getMemberVotes() {
        return memberVotes;
    }

    public void setMemberVotes(int memberVotes) {
        this.memberVotes = memberVotes;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Set<Long> getMemberVotesIds() {
        return memberVotesIds;
    }

    public void setMemberVotesIds(Set<Long> memberVotesIds) {
        this.memberVotesIds = memberVotesIds;
    }
}
