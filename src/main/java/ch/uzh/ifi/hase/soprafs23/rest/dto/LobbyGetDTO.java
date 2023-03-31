package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import java.time.LocalDate;

public class LobbyGetDTO {

  private Long userId;
  private String email;
  private String username;
  private UserStatus status;
  private String token;
  private LocalDate creationDate;

  private LocalDate birthdate;

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
}
