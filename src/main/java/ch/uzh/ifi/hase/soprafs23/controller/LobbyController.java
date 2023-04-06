package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserLobbyDTO;
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
  private final UserService userService;

  LobbyController(LobbyService lobbyService, UserService userService) {

      this.lobbyService = lobbyService;
      this.userService = userService;
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

  //registration
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
    // convert API user to internal representation
    Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

    Lobby createdLobby = lobbyService.createLobby(lobbyInput);

    lobbyService.addMember(lobbyInput.getLobbyId(), lobbyPostDTO.getHostMemberId());

    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
  }
  @PutMapping("{lobbyId}/join")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void joinLobby(@RequestBody UserLobbyDTO userLobbyDTO, @PathVariable Long lobbyId) {
    lobbyService.addMember(lobbyId, userLobbyDTO.getUserId());
  }

}
