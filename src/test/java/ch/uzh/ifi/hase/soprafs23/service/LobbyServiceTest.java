package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.Member;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MemberRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private User testUser;
    private Lobby testLobby;

    private Member testMember;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);

        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);

        List<LocalDateTime> selectedDates = new ArrayList<>();
        selectedDates.add(LocalDateTime.now());

        List<Location> selectedLocations = new ArrayList<>();

        Location testLocation = new Location();
        testLocation.setLatitude(0.0);
        testLocation.setLongitude(0.0);
        selectedLocations.add(testLocation);

        List<String> selectedSports = new ArrayList<>();
        selectedSports.add("Football");

        Member testMember = new Member();

        testMember.setMemberIdId(1L);
        testMember.setUserId(1L);
        testMember.setLobbyId(1L);
        testMember.setUser(testUser);
        testMember.setEmail("testEmail");
        testMember.setUsername("testUsername");
        testMember.setSelectedDates(selectedDates);
        testMember.setSelectedLocations(selectedLocations);
        testMember.setSelectedSports(selectedSports);


        Mockito.when(memberRepository.save(Mockito.any())).thenReturn(testMember);
    }


    @Test
    void createLobby_validInputs() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testLobby.getLobbyName(), createdLobby.getLobbyName());
        assertEquals(testLobby.getLobbyRegion(), createdLobby.getLobbyRegion());
        assertEquals(testLobby.getLobbyMaxMembers(), createdLobby.getLobbyMaxMembers());
        assertEquals(testLobby.getLobbyTimeLimit(), createdLobby.getLobbyTimeLimit());
        assertNotNull(createdLobby.getToken());
    }

    @Test
    public void createLobby_duplicateName_throwsException() {
        // given -> a first user has already been created
        lobbyService.createLobby(testLobby);

        // when -> setup additional mocks for UserRepository
        Mockito.when(lobbyRepository.findByLobbyName(Mockito.any())).thenReturn(testLobby);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby));
    }

    @Test
    void getLobby_lobbyExists() {
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);

        lobbyService.createLobby(testLobby);

        Mockito.when(lobbyRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testLobby));

        Lobby getLobby = lobbyService.getLobby(testLobby.getLobbyId());

        assertEquals(testLobby.getLobbyId(), getLobby.getLobbyId());
        assertEquals(testLobby.getLobbyName(), getLobby.getLobbyName());
        assertEquals(testLobby.getLobbyRegion(), getLobby.getLobbyRegion());
        assertEquals(testLobby.getLobbyMaxMembers(), getLobby.getLobbyMaxMembers());
        assertEquals(testLobby.getLobbyTimeLimit(), getLobby.getLobbyTimeLimit());
        assertEquals(testLobby.getToken(), getLobby.getToken());
    }

    @Test
    public void getLobby_lobbyDoesNotExist_throwsException() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobby(testLobby.getLobbyId()));
    }

    @Test
    void getUser_userExists() {
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        User getUser = lobbyService.getUser(testUser.getUserId());

        assertEquals(testUser.getUserId(), getUser.getUserId());
        assertEquals(testUser.getUsername(), getUser.getUsername());
        assertEquals(testUser.getEmail(), getUser.getEmail());
        assertEquals(testUser.getPassword(), getUser.getPassword());

    }

    @Test
    public void getUser_userDoesNotExist_throwsException() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.getUser(testUser.getUserId()));
    }

    @Test
    void getMember() {
    }

    @Test
    void getMemberById() {
    }

    @Test
    void addMember() {
    }

    @Test
    void removeMember() {
    }

    @Test
    void deleteLobby() {
    }

    @Test
    void setSports() {
    }

    @Test
    void setLocations() {
    }

    @Test
    void setDates() {
    }

    @Test
    void lockSelections() {
    }

    @Test
    void unlockSelections() {
    }

    @Test
    void addLobbyLocation() {
    }

    @Test
    void addLobbyLocationVote() {
    }

    @Test
    void removeLobbyLocationVote() {
    }

    @Test
    void getMembers() {
    }

    @Test
    void getLocations() {
    }
}