package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", initialValue = 1)
  private Long userId;

  @Column(nullable = true)
  private String email;

  @Column(nullable = true, unique = true)
  private String username;

  @Column(nullable = true, unique = false)
  private String password;

  @Column(nullable = true, unique = true)
  private String token;

  @Column(nullable = true)
  private UserStatus status;

  @Column(nullable = true, unique = false, updatable = false)
  private LocalDate creationDate;

  @Column
  private LocalDate birthdate;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Participant> participants = new ArrayList<>();
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Lobby> lobbies = new ArrayList<>();
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Event> events = new ArrayList<>();

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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
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

  public String getPassword() {
        return password;
    }

  public void setPassword(String password) {
        this.password = password;
    }

  public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

  public LocalDate getBirthdate(){
        return birthdate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }
    public void removeEvent(Event eventToRemove) {
        events.removeIf(event -> event.equals(eventToRemove));
    }
    public void addLobby(Lobby lobby) {
        this.lobbies.add(lobby);
    }
    public void removeLobby(Lobby lobbyToRemove) {
        lobbies.removeIf(lobby -> lobby.equals(lobbyToRemove));
    }

}
