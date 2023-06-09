package ch.uzh.ifi.hase.soprafs23.rest.dto;


public class LobbyPostDTO {
  private String lobbyName;
  private String lobbyRegion;
  private String lobbyRegionShortCode;
  private Integer lobbyMaxMembers;
  private Integer lobbyTimeLimit;
  private Long hostMemberId;
  private String hostMemberToken;


  public Long getHostMemberId() {return hostMemberId; }

  public void setHostMemberId(Long hostMemberId) {this.hostMemberId = hostMemberId; }

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

  public String getLobbyRegionShortCode() {
        return lobbyRegionShortCode;
    }

  public void setLobbyRegionShortCode(String lobbyRegionShortCode) {
      this.lobbyRegionShortCode = lobbyRegionShortCode;
  }

  public Integer getLobbyMaxMembers() {return lobbyMaxMembers; }

  public void setLobbyMaxMembers(Integer lobbyMaxMembers) {this.lobbyMaxMembers = lobbyMaxMembers; }

  public Integer getLobbyTimeLimit() {return lobbyTimeLimit; }

  public void setLobbyTimeLimit(Integer lobbyTimeLimit) {this.lobbyTimeLimit = lobbyTimeLimit; }

  public String getHostMemberToken() {
        return hostMemberToken;
    }

  public void setHostMemberToken(String hostMemberToken) {
        this.hostMemberToken = hostMemberToken;
    }
}

