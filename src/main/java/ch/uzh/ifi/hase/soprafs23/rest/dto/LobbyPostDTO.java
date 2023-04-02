package ch.uzh.ifi.hase.soprafs23.rest.dto;


import ch.uzh.ifi.hase.soprafs23.entity.Member;

import javax.persistence.criteria.CriteriaBuilder;

public class LobbyPostDTO {
  private String lobbyName;
  private String lobbyRegion;
  private Long lobbyId;
  private Integer lobbyMaxMembers;
  private Integer lobbyTimeLimit;

  private Member hostMember;


  public Member getHostMember() {return hostMember; }

  public void setHostMember(Member hostMember) {this.hostMember = hostMember; }

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

  public Long getLobbyId() {
        return lobbyId;
    }

  public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

  public Integer getLobbyMaxMembers() {return lobbyMaxMembers; }

  public void setLobbyMaxMembers(Integer lobbyMaxMembers) {this.lobbyMaxMembers = lobbyMaxMembers; }

  public Integer getLobbyTimeLimit() {return lobbyTimeLimit; }

  public void setLobbyTimeLimit(Integer lobbyTimeLimit) {this.lobbyTimeLimit = lobbyTimeLimit; }

}

