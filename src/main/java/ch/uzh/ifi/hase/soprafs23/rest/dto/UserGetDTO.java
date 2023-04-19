package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.time.LocalDate;
import java.util.List;

public class UserGetDTO {

  private Long userId;
  private String email;
  private String username;
  private UserStatus status;
  private String token;
  private LocalDate creationDate;
  private LocalDate birthdate;
  private String bio;
  private List<UserEventGetDTO> userEventGetDTOs;

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

  public String getToken() {
        return token;
    }
  public void setToken(String token) {
        this.token = token;
    }
  public LocalDate getCreationDate() {
        return creationDate;
    }
  public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

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

    public List<UserEventGetDTO> getEventGetDTOs() {
        return userEventGetDTOs;
    }

    public void setUserEventGetDTOs(List<UserEventGetDTO> eventGetDTOS) {
        this.userEventGetDTOs = eventGetDTOS;
    }
}
