package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.Member;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Lobby Controller
 * This class is responsible for handling all REST request that are related to
 * the lobby.
 * The controller will receive the request and delegate the execution to the
 * LobbyService and finally return the result.
 */
@RestController
@RequestMapping("/lobbies")
public class LobbyController {

  private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
      this.lobbyService = lobbyService;
    }

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<LobbyGetDTO> getAllLobbies() {
    // fetch all users in the internal representation
    List<Lobby> lobbies = lobbyService.getLobbies();
    List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (Lobby lobby : lobbies) {
      lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }
    return lobbyGetDTOs;
  }
  @GetMapping("/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public LobbyGetDTO getLobby(@PathVariable Long lobbyId) {
    Lobby lobby = lobbyService.getLobby(lobbyId);
      return lobbyService.updateLobby(lobby);
  }

  //registration
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public MemberDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
    // convert API user to internal representation
    Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

    lobbyService.createLobby(lobbyInput);

    Member lobbyCreator = lobbyService.addMember(lobbyInput.getLobbyId(), lobbyPostDTO.getHostMemberId());

    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToMemberDTO(lobbyCreator);
  }
  @PutMapping("{lobbyId}/join")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public MemberDTO joinLobby(@RequestBody UserLobbyDTO userLobbyDTO, @PathVariable Long lobbyId) {
    Member createdMember = lobbyService.addMember(lobbyId, userLobbyDTO.getUserId());
    return DTOMapper.INSTANCE.convertEntityToMemberDTO(createdMember);
  }
  @PutMapping("{lobbyId}/leave")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void leaveLobby(@RequestBody UserLobbyDTO userLobbyDTO, @PathVariable Long lobbyId) {
    lobbyService.removeMember(lobbyId, userLobbyDTO.getUserId());
  }
  @DeleteMapping("{lobbyId}/delete")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteEvent(@PathVariable Long lobbyId) {
    lobbyService.deleteLobby(lobbyId);
  }
  @PutMapping("{lobbyId}/sport")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public MemberDTO setSports(@RequestBody MemberSportDTO sportDTO, @PathVariable Long lobbyId) {
    Member member = lobbyService.setSports(lobbyId, sportDTO.getMemberId(), sportDTO.getSelectedSports());
    return DTOMapper.INSTANCE.convertEntityToMemberDTO(member);
  }
  @PutMapping("{lobbyId}/date")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public MemberDTO setDates(@RequestBody MemberDateDTO dateDTO, @PathVariable Long lobbyId) {
      Member member = lobbyService.setDates(lobbyId, dateDTO.getMemberId(), dateDTO.getSelectedDates());
      return DTOMapper.INSTANCE.convertEntityToMemberDTO(member);
  }
  @PutMapping("{lobbyId}/lock")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public MemberDTO lockSelections(@RequestBody MemberLockDTO lockDTO, @PathVariable Long lobbyId) {
      Member member = lobbyService.lockSelections(lobbyId, lockDTO.getMemberId());
      return DTOMapper.INSTANCE.convertEntityToMemberDTO(member);
  }

  @PutMapping("{lobbyId}/unlock")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public MemberDTO unlockSelections(@RequestBody MemberLockDTO lockDTO, @PathVariable Long lobbyId) {
      Member member = lobbyService.unlockSelections(lobbyId, lockDTO.getMemberId());
      return DTOMapper.INSTANCE.convertEntityToMemberDTO(member);
  }
  @PostMapping("{lobbyId}/locations")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LobbyGetDTO addLobbyLocation(@RequestBody LobbyLocationDTO lobbyLocationDTO, @PathVariable Long lobbyId) {
      Location location = DTOMapper.INSTANCE.convertLobbyLocationDTOtoEntity(lobbyLocationDTO);
      location.setLocationType("OTHER");
      lobbyService.addLobbyLocation(lobbyId, location);
      return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobbyService.getLobby(lobbyId));
  }
  @DeleteMapping("{lobbyId}/locations")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void removeLobbyLocation(@RequestBody MemberLocationDTO memberLocationDTO, @PathVariable Long lobbyId) {
      lobbyService.removeLobbyLocation(lobbyId, memberLocationDTO.getMemberId());
  }
  
  @PutMapping("{lobbyId}/locations/{locationId}/vote")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void addLobbyLocationVote(@RequestBody MemberLocationDTO memberlocationDTO, @PathVariable Long lobbyId,
                                   @PathVariable Long locationId) {
      lobbyService.addLobbyLocationVote(lobbyId, memberlocationDTO.getMemberId(), locationId);
  }
  @PutMapping("{lobbyId}/locations/{locationId}/unvote")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void removeLobbyLocationVote(@RequestBody MemberLocationDTO memberlocationDTO, @PathVariable Long lobbyId,
                                   @PathVariable Long locationId) {
      lobbyService.removeLobbyLocationVote(lobbyId, memberlocationDTO.getMemberId(), locationId);
  }

  @PostMapping("{lobbyId}/messages")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LobbyGetDTO addLobbyMessage(@RequestBody MessageDTO messageDTO, @PathVariable Long lobbyId) {
      lobbyService.addLobbyMessage(lobbyId, messageDTO);
      return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobbyService.getLobby(lobbyId));
  }


  //TESTS
  @GetMapping("/test")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Lobby> getAllLobbiesTest() {
        return lobbyService.getLobbies();
    }

  @GetMapping("/membersTest")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Member> getMembers() {
      return lobbyService.getMembers();
  }

  @GetMapping("/locationsTest")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Location> getLocations() {
        return lobbyService.getLocations();
    }
}