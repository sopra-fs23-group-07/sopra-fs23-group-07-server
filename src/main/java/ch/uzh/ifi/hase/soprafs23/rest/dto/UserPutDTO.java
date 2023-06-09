package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.time.LocalDate;


public class UserPutDTO {
    private Long userId;
    private String password;
    private String username;
    private String email;
    private UserStatus userStatus;
    private String token;
    private LocalDate birthdate;
    private String bio;
    private Long avatar;

    public Long getUserId() {return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() {return username; }

    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password; }

    public void setPassword(String password) {this.password = password;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}


    public UserStatus getUserStatus() {return userStatus;}

    public void setUserStatus(UserStatus userStatus) {this.userStatus = userStatus;}

    public LocalDate getBirthdate(){
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate){
        this.birthdate = birthdate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }
}