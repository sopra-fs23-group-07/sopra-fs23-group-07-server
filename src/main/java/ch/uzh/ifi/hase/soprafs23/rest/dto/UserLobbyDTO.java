package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserLobbyDTO {
    private Long userId;
    private String token;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}