package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserPostDTO {
  private String email;
  private String username;
  private String password;
  private Long userId;

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
  public String getPassword() {
        return password;
    }
  public void setPassword(String password) {
        this.password = password;
    }
  public Long getUserId() {
        return userId;
    }
  public void setUserId(Long userId) {
        this.userId = userId;
    }
}