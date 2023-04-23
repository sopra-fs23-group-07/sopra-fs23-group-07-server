package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LobbyService lobbyService;

  private Lobby lobby;

  private LobbyGetDTO lobbyGetDTO;

  private User user;

  private Member member;

    @BeforeEach
    public void setup() {
        // given
        user = new User();
        user.setPassword("password");
        user.setUsername("username");
        user.setUserId(1L);
        user.setEmail("user@user.com");
        user.setToken("token");

        member = new Member();
        member.setUser(user);
        member.setMemberIdId(1L);
        member.setUserId(1L);
        member.setUsername("username");
        member.setLobbyId(1L);

        lobby = new Lobby();
        lobby.setLobbyName("lobby");
        lobby.setLobbyMaxMembers(10);
        lobby.setLobbyRegion("ZH");
        lobby.setLobbyTimeLimit(10);
        lobby.setLobbyId(1L);
        lobby.setHostMemberId(1L);
        lobby.setToken("token");

        Timer timer = new Timer();
        timer.setStartTime(LocalDateTime.now());
        timer.setLobby(lobby);

        lobby.setTimer(timer);

        lobbyGetDTO = new LobbyGetDTO();
        lobbyGetDTO.setLobbyName("lobby");
        lobbyGetDTO.setLobbyMaxMembers(10);
        lobbyGetDTO.setLobbyRegion("ZH");
        lobbyGetDTO.setLobbyTimeLimit(10);
        lobbyGetDTO.setLobbyId(1L);
        lobbyGetDTO.setToken("token");
    }

  @Test
  public void givenLobbies_whenGetLobbies_thenReturnJsonArray() throws Exception {

    List<Lobby> allLobbies = Collections.singletonList(lobby);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(lobbyService.getLobbies()).willReturn(allLobbies);

    // when
    MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].lobbyName", is(lobby.getLobbyName())))
        .andExpect(jsonPath("$[0].lobbyMaxMembers", is(lobby.getLobbyMaxMembers())))
        .andExpect(jsonPath("$[0].lobbyRegion", is(lobby.getLobbyRegion())))
            .andExpect(jsonPath("$[0].lobbyTimeLimit", is(lobby.getLobbyTimeLimit())))
            .andExpect(jsonPath("$[0].lobbyId", is(1)));
  }

//    @Test
//    public void givenLobby_whenGetLobby_thenReturnJsonArray() throws Exception {
//
//
//        given(lobbyService.getLobby(Mockito.anyLong())).willReturn(lobby);
//        given(lobbyService.updateLobby(Mockito.any())).willReturn(lobbyGetDTO);
//
//        // when
//        MockHttpServletRequestBuilder getRequest = get("/lobbies/" + lobby.getLobbyId()).contentType(MediaType.APPLICATION_JSON);
//
//        // then
//        mockMvc.perform(getRequest).andExpect(status().isOk())
//                .andExpect(jsonPath("$.lobbyName", is(lobby.getLobbyName())))
//                .andExpect(jsonPath("$.lobbyMaxMembers", is(lobby.getLobbyMaxMembers())))
//                .andExpect(jsonPath("$.lobbyRegion", is(lobby.getLobbyRegion())))
//                .andExpect(jsonPath("$.lobbyTimeLimit", is(lobby.getLobbyTimeLimit())))
//                .andExpect(jsonPath("$.lobbyId", is(1)));
//    }

//    @Test
//    public void givenLobbyGetDTO_createLobby_thenReturnsJsonArray() throws Exception {
//        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
//        lobbyPostDTO.setLobbyName("name");
//        lobbyPostDTO.setLobbyRegion("ZH");
//        lobbyPostDTO.setLobbyTimeLimit(10);
//        lobbyPostDTO.setLobbyMaxMembers(10);
//        lobbyPostDTO.setHostMemberId(1L);
//
//        MemberDTO memberDTO = new MemberDTO();
//        memberDTO.setLobbyId(lobby.getLobbyId());
//        memberDTO.setUserId(user.getUserId());
//        memberDTO.setMemberId(member.getMemberId());
//        memberDTO.setUsername(user.getUsername());
//
//        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);
//        given(lobbyService.addMember(Mockito.anyLong(), Mockito.anyLong())).willReturn(member);
//
//        given(DTOMapper.INSTANCE.convertEntityToMemberDTO(Mockito.any())).willReturn(memberDTO);
//
//        MockHttpServletRequestBuilder postRequest = post("/lobbies")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(lobbyPostDTO));
//
//        // then
//        mockMvc.perform(postRequest).andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username", is(member.getUsername())))
//                .andExpect(jsonPath("$.userId", is(1)))
//                .andExpect(jsonPath("$.memberId", is(1)))
//                .andExpect(jsonPath("$.lobbyId", is(1)));
//    }

    @Test
    public void givenLobbyAndUser_userJoinsLobby_thenReturnJsonArray() throws Exception {
        UserLobbyDTO userLobbyDTO = new UserLobbyDTO();
        userLobbyDTO.setUserId(user.getUserId());

        given(lobbyService.addMember(Mockito.anyLong(), Mockito.anyLong())).willReturn(member);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/join", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLobbyDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(member.getUsername())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.lobbyId", is(1)));
    }

    @Test
    public void givenLobbyAndUser_userLeavesLobby() throws Exception {
        UserLobbyDTO userLobbyDTO = new UserLobbyDTO();
        userLobbyDTO.setUserId(user.getUserId());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/leave", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLobbyDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    @Test
    public void givenLobby_lobbyIsDeleted() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{lobbyId}/delete", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(deleteRequest).andExpect(status().isOk());
    }


    @Test
    public void givenLobbyAndMember_memberAddsSport_thenReturnJsonArray() throws Exception {
        MemberSportDTO memberSportDTO = new MemberSportDTO();
        memberSportDTO.setMemberId(member.getMemberId());

        List<String> sports = new ArrayList<>();
        sports.add("Football");

        memberSportDTO.setSelectedSports(sports);

        Member memberWithSport = new Member();
        memberWithSport = member;
        memberWithSport.setSelectedSports(sports);

        given(lobbyService.setSports(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList())).willReturn(memberWithSport);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/sport", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberSportDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(member.getUsername())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.lobbyId", is(1)))
                .andExpect(jsonPath("$.selectedSports", is(sports)));
    }


    @Test
    public void givenLobbyAndMember_memberAddsDate_thenReturnJsonArray() throws Exception {
        MemberDateDTO memberDateDTO = new MemberDateDTO();
        memberDateDTO.setMemberId(member.getMemberId());

        List<String> datesString = new ArrayList<>();
        List<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime time = LocalDateTime.of(2023, 04, 24, 14, 33, 48, 0);
        dates.add(time);
        datesString.add(time.toString());

        memberDateDTO.setSelectedDates(datesString);

        Member memberWithDate = new Member();
        memberWithDate = member;
        memberWithDate.setSelectedDates(dates);

        given(lobbyService.setDates(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList())).willReturn(memberWithDate);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/date", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberDateDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(member.getUsername())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.lobbyId", is(1)))
                .andExpect(jsonPath("$.selectedDates", is(datesString)));
    }

    @Test
    public void givenLobbyAndMember_memberLocksChoices_thenReturnsJsonArray() throws Exception {
        MemberLockDTO memberLockDTO = new MemberLockDTO();
        memberLockDTO.setMemberId(member.getMemberId());

        Member memberLocked = member;
        memberLocked.setHasLockedSelections(true);

        given(lobbyService.lockSelections(Mockito.anyLong(), Mockito.anyLong())).willReturn(memberLocked);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/lock", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberLockDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(member.getUsername())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.lobbyId", is(1)))
                .andExpect(jsonPath("$.hasLockedSelections", is(true)));
    }

    @Test
    public void givenLobbyAndMember_memberUnlocksChoices_thenReturnsJsonArray() throws Exception {
        MemberLockDTO memberLockDTO = new MemberLockDTO();
        memberLockDTO.setMemberId(member.getMemberId());

        given(lobbyService.unlockSelections(Mockito.anyLong(), Mockito.anyLong())).willReturn(member);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/unlock", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberLockDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(member.getUsername())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.lobbyId", is(1)))
                .andExpect(jsonPath("$.hasLockedSelections", is(false)));
    }

    @Test
    public void givenLobbyAndLocation_addLobbyLocation_thenReturnsJsonArray() throws Exception {
        LobbyLocationDTO lobbyLocationDTO = new LobbyLocationDTO();
        lobbyLocationDTO.setLobbyId(lobby.getLobbyId());
        lobbyLocationDTO.setMemberId(member.getMemberId());
        lobbyLocationDTO.setLatitude(0.0);
        lobbyLocationDTO.setLongitude(0.0);


        Location location = new Location();
        location.setLobbyId(lobby.getLobbyId());
        location.setMemberId(member.getMemberId());
        location.setLatitude(0.0);
        location.setLongitude(0.0);

        Lobby lobbyWithLocation = lobby;
        lobbyWithLocation.addLobbyLocation(location);

        given(lobbyService.getLobby(Mockito.anyLong())).willReturn(lobbyWithLocation);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{lobbyId}/locations", lobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyLocationDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyName", is(lobby.getLobbyName())))
                .andExpect(jsonPath("$.lobbyMaxMembers", is(lobby.getLobbyMaxMembers())))
                .andExpect(jsonPath("$.lobbyRegion", is(lobby.getLobbyRegion())))
                .andExpect(jsonPath("$.lobbyTimeLimit", is(lobby.getLobbyTimeLimit())))
                .andExpect(jsonPath("$.lobbyId", is(Integer.valueOf(Math.toIntExact(lobby.getLobbyId())))))
                .andExpect(jsonPath("$.lobbyLocationDTOs[0].memberId", is(Integer.valueOf(Math.toIntExact(member.getMemberId())))))
                .andExpect(jsonPath("$.lobbyLocationDTOs[0].lobbyId", is(Integer.valueOf(Math.toIntExact(lobby.getLobbyId())))))
                .andExpect(jsonPath("$.lobbyLocationDTOs[0].longitude", is(lobbyLocationDTO.getLongitude())))
                .andExpect(jsonPath("$.lobbyLocationDTOs[0].latitude", is(lobbyLocationDTO.getLatitude())));

    }

    @Test
    public void givenLobbyAndMember_memberVotesOnALocation_thenReturnsJsonArray() throws Exception {
        Location location = new Location();
        location.setLocationId(1L);
        location.setLobbyId(lobby.getLobbyId());
        location.setMemberId(member.getMemberId());
        location.setLatitude(0.0);
        location.setLongitude(0.0);

        MemberLocationDTO memberLocationDTO = new MemberLocationDTO();
        memberLocationDTO.setMemberId(member.getMemberId());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/locations/{locationId}/vote", lobby.getLobbyId(), location.getLocationId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberLocationDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk());

    }

    @Test
    public void givenLobbyAndMember_memberUnvotesOnALocation_thenReturnsJsonArray() throws Exception {
        Location location = new Location();
        location.setLocationId(1L);
        location.setLobbyId(lobby.getLobbyId());
        location.setMemberId(member.getMemberId());
        location.setLatitude(0.0);
        location.setLongitude(0.0);

        MemberLocationDTO memberLocationDTO = new MemberLocationDTO();
        memberLocationDTO.setMemberId(member.getMemberId());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}/locations/{locationId}/unvote", lobby.getLobbyId(), location.getLocationId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberLocationDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk());

    }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}