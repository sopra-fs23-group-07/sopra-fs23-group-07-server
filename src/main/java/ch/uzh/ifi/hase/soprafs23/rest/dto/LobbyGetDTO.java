package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.OverlapColor;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.EventDetails;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.Member;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class LobbyGetDTO {

  private Long lobbyId;

  private String lobbyName;

  private ArrayList<Member> lobbyMembers;

  private Integer lobbyMaxMembers;

  private String lobbyRegion;

  private Integer lobbyTimeLimit;

  private String token;

  private String lobbyDecidedLocation;

  private String lobbyDecidedSport;

  private Date lobbyDecidedDate;

  private ArrayList<Location> lobbyLocations;



  public void addLobbyMember(User user) {}

  public void removeLobbyMember(User user) {}

  public Long getLobbyId() {
        return lobbyId;
    }

  public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

  public ArrayList<Member> getLobbyMembers() {return this.lobbyMembers; }

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
