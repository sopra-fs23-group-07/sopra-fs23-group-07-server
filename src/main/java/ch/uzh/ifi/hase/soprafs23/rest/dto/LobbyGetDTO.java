package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.entity.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LobbyGetDTO {

  private Long lobbyId;

  private String lobbyName;

  private Integer lobbyMaxMembers;

  private String lobbyRegion;

  private Integer lobbyTimeLimit;

  private String token;

  private String lobbyDecidedLocation;

  private String lobbyDecidedSport;

  private LocalDateTime lobbyDecidedDate;

  private ArrayList<Location> lobbyLocations;
  private List<MemberDTO> memberDTOS;


  public ArrayList<Location> getLobbyLocations() {return lobbyLocations; }

  public void setLobbyLocations(ArrayList<Location> lobbyLocations) {this.lobbyLocations = lobbyLocations; }

  public Long getLobbyId() {
        return lobbyId;
    }

  public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

  public Integer getLobbyMaxMembers() {return this.lobbyMaxMembers;}

  public void setLobbyMaxMembers(Integer lobbyMaxMembers) {this.lobbyMaxMembers = lobbyMaxMembers; }

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

  public LocalDateTime getLobbyDecidedDate() {return lobbyDecidedDate;}

  public void setLobbyDecidedDate(LocalDateTime lobbyDecidedDate) {this.lobbyDecidedDate = lobbyDecidedDate;}

  public String getLobbyDecidedLocation() {return lobbyDecidedLocation;}

  public void setLobbyDecidedLocation(String lobbyDecidedLocation) {this.lobbyDecidedLocation = lobbyDecidedLocation;}

  public String getToken() {
        return token;
    }

  public void setToken(String token) {
        this.token = token;
    }

  public List<MemberDTO> getMemberDTOs() {
      return memberDTOS;
  }

  public void setMemberDTOs(List<MemberDTO> memberDTOS) {
      this.memberDTOS = memberDTOS;
  }
}
