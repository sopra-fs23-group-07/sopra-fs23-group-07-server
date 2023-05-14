package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public class LobbyGetDTO {

  private Long lobbyId;

  private String lobbyName;

  private Integer lobbyMaxMembers;

  private String lobbyRegion;


    private String lobbyRegionShortCode;

  private Integer lobbyTimeLimit;

  private String token;

  private LobbyLocationDTO lobbyDecidedLocation;

  private String lobbyDecidedSport;

  private LocalDateTime lobbyDecidedDate;

  private Long createdEventId;

  private List<LobbyLocationDTO> lobbyLocationDTOs;

  private List<MessageDTO> lobbyMessageDTOs;
  private List<MemberDTO> memberDTOS;

  private Integer lobbyMembersCount;

  private boolean haveAllMembersLockedSelections;
  private Long timeRemaining;




  public Long getCreatedEventId() {return createdEventId;}

  public void setCreatedEventId(Long createdEventId) {this.createdEventId = createdEventId;}

  public void setHaveAllMembersLockedSelections(boolean haveAllMembersLockedSelections) {this.haveAllMembersLockedSelections = haveAllMembersLockedSelections;}

  public boolean isHaveAllMembersLockedSelections() {return haveAllMembersLockedSelections;}

  public Integer getLobbyMembersCount() {return memberDTOS.size();}

  public void setLobbyMembersCount(Integer lobbyMembersCount) {this.lobbyMembersCount = lobbyMembersCount;}

  public List<LobbyLocationDTO> getLobbyLocationDTOs() {return lobbyLocationDTOs; }

  public void setLobbyLocationDTOs(List<LobbyLocationDTO> lobbyLocations) {this.lobbyLocationDTOs = lobbyLocations; }

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

    public String getLobbyRegionShortCode() {
        return lobbyRegionShortCode;
    }

    public void setLobbyRegionShortCode(String lobbyRegionShortCode) {
        this.lobbyRegionShortCode = lobbyRegionShortCode;
    }


  public Integer getLobbyTimeLimit() {return this.lobbyTimeLimit; }

  public void setLobbyTimeLimit(Integer lobbyTimeLimit) {this.lobbyTimeLimit = lobbyTimeLimit; }

  public String getLobbyDecidedSport() {return lobbyDecidedSport; }

  public void setLobbyDecidedSport(String lobbyDecidedSport) {this.lobbyDecidedSport = lobbyDecidedSport;}

  public LocalDateTime getLobbyDecidedDate() {return lobbyDecidedDate;}

  public void setLobbyDecidedDate(LocalDateTime lobbyDecidedDate) {this.lobbyDecidedDate = lobbyDecidedDate;}

  public LobbyLocationDTO getLobbyDecidedLocation() {return lobbyDecidedLocation;}

  public void setLobbyDecidedLocation(LobbyLocationDTO lobbyDecidedLocation) {this.lobbyDecidedLocation = lobbyDecidedLocation;}

    public List<MessageDTO> getLobbyMessageDTOs() {
        return lobbyMessageDTOs;
    }

    public void setLobbyMessageDTOs(List<MessageDTO> lobbyMessageDTOs) {
        this.lobbyMessageDTOs = lobbyMessageDTOs;
    }

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

    public Long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(Long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}
