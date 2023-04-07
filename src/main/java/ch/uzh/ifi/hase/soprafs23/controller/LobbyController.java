package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Member;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
@RequestMapping("/lobbies")
public class LobbyController {

  private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService, UserService userService) {

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
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
  }

  //registration
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public MemberDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
    // convert API user to internal representation
    Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

    Lobby createdLobby = lobbyService.createLobby(lobbyInput);

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
  public void setSports(@RequestBody SportDTO sportDTO, @PathVariable Long lobbyId) {
    lobbyService.setSports(lobbyId, sportDTO.getMemberId(), sportDTO.getSelectedSports());
  }
  @PutMapping("{lobbyId}/location")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void setLocations(@RequestBody LocationDTO locationDTO, @PathVariable Long lobbyId) {
    lobbyService.setLocations(lobbyId, locationDTO.getMemberId(), locationDTO.getSelectedLocations());
  }
  @PutMapping("{lobbyId}/date")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void setDates(@RequestBody DateDTO dateDTO, @PathVariable Long lobbyId) {
      lobbyService.setDates(lobbyId, dateDTO.getMemberId(), dateDTO.getSelectedDates());
  }
  @PutMapping("{lobbyId}/lock")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void lockSelections(@RequestBody LockDTO lockDTO, @PathVariable Long lobbyId) {
      lobbyService.lockSelections(lobbyId, lockDTO.getMemberId());
  }



  @GetMapping("/members")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Member> getMembers() {
      return lobbyService.getMembers();
  }

}
