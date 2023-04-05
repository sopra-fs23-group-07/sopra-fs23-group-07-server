package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.OverlapColor;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long lobbyId;

  @Column(nullable = false, unique = true)
  private String lobbyName;

  @Column(nullable = false, unique = true)
  private Long hostMemberId;

  @Column(nullable = true)
  private ArrayList<Member> lobbyMembers;

  @Column(nullable = false)
  private Integer lobbyMaxMembers;

  @Column(nullable = false)
  private String lobbyRegion;

  @Column(nullable = false)
  private Integer lobbyTimeLimit;

  @Column(nullable = true)
  private String lobbyDecidedLocation;

  @Column(nullable = true)
  private String lobbyDecidedSport;

  @Column(nullable = true)
  private Date lobbyDecidedDate;

  @Column(nullable = true)
  private ArrayList<Location> lobbyLocations;

  @Column(nullable = false, unique = true)
  private String token;


  public Long getHostMemberId() {return hostMemberId; }

  public void setHostMemberId(Long hostMemberId) {this.hostMemberId = hostMemberId; }

  public ArrayList<Location> getLobbyLocations() {return lobbyLocations; }

  public void addLobbyLocation(Location location) {}

  public void removeLobbyLocation(Location location) {}

  public boolean isLobbyFull() {return false;}

  public boolean isLobbyEmpty() {return false;}

  public boolean hasAllMembersLockedSelections() {return false;}

  public boolean hasTimerRunOut() {return true;}

  public void decideAllSelections() {}

  public EventDetails createEventDetails() {return new EventDetails();}

  public OverlapColor checkSportOverlap(String sport) {return OverlapColor.RED;}

  public OverlapColor checkLocationOverlap(Location location) {return OverlapColor.RED;}

  public OverlapColor checkDateOverlap(Date date) {return OverlapColor.RED;}

  public void addLobbyMember(Member member) {}

  public void removeLobbyMember(Member member) {}

  public Long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(Long lobbyId) {
    this.lobbyId = lobbyId;
  }

  public ArrayList<Member> getLobbyMembers() {return this.lobbyMembers; }

  public void setLobbyMaxMembers(Integer lobbyMaxMembers) {this.lobbyMaxMembers = lobbyMaxMembers; }

  public Integer getLobbyMaxMembers() {return this.lobbyMaxMembers;}

  public String getLobbyName() {
    return lobbyName;
  }

  public void setLobbyName(String lobbyName) {
    this.lobbyName = lobbyName;
  }

  public String getLobbyRegion() {
    return lobbyRegion;
  }

  public void setLobbyRegion(String lobbyRegion) {
    this.lobbyRegion = lobbyRegion;
  }

  public Integer getLobbyTimeLimit() {return this.lobbyTimeLimit; }

  public void setLobbyTimeLimit(Integer lobbyTimeLimit) {this.lobbyTimeLimit = lobbyTimeLimit; }

  public String getLobbyDecidedSport() {return lobbyDecidedSport; }

  public void setLobbyDecidedSport(String lobbyDecidedSport) {this.lobbyDecidedSport = lobbyDecidedSport;}

  public Date getLobbyDecidedDate() {return lobbyDecidedDate;}

  public void setLobbyDecidedDate(Date lobbyDecidedDate) {this.lobbyDecidedDate = lobbyDecidedDate;}

  public String getLobbyDecidedLocation() {return lobbyDecidedLocation;}

  public void setLobbyDecidedLocation(String lobbyDecidedLocation) {this.lobbyDecidedLocation = lobbyDecidedLocation;}

  public Integer getNumberOfVotesForLocation(Location location) {return 0;}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


}
