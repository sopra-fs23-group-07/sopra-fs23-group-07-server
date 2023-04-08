package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class LobbyLocationDTO {
    private Long memberId;
    private String location;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
