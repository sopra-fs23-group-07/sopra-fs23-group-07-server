package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.time.LocalDate;


public class UserPutDTO {
    private Long userId;
    private String password;
    private String username;
    private String name;
    private UserStatus userStatus;
    private String token;

    private LocalDate birthdate;

    public Long getUserId() {return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() {return username; }

    public void setUsername(String username) {this.username = username;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getPassword() {return password; }

    public void setPassword(String password) {this.password = password;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}


    public UserStatus getUserStatus() {return userStatus;}

    public void setUserStatus(UserStatus userStatus) {this.userStatus = userStatus;}

    public LocalDate getBirthdate(){
        return birthdate;
    }
}