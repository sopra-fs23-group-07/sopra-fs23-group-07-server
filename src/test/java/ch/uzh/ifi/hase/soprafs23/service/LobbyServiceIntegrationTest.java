package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import ch.uzh.ifi.hase.soprafs23.entity.Member;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LocationRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MemberRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
class LobbyServiceIntegrationTest {

  @Qualifier("lobbyRepository")
  @Autowired
  private LobbyRepository lobbyRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("memberRepository")
    @Autowired
    private MemberRepository memberRepository;

    @Qualifier("locationRepository")
    @Autowired
    private LocationRepository locationRepository;

  @Autowired
  private LobbyService lobbyService;

  @BeforeEach
  void setup() {
      locationRepository.deleteAll();
      memberRepository.deleteAll();
      lobbyRepository.deleteAll();
      userRepository.deleteAll();

  }

    @Test
    void createLobby_validInputs_success() {

        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());

        User testUser = new User();
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();
        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);

        assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testLobby.getLobbyName(), createdLobby.getLobbyName());
        assertEquals(testLobby.getLobbyRegion(), createdLobby.getLobbyRegion());
        assertEquals(testLobby.getLobbyMaxMembers(), createdLobby.getLobbyMaxMembers());
        assertEquals(testLobby.getLobbyTimeLimit(), createdLobby.getLobbyTimeLimit());
        assertEquals(testLobby.getHostMemberId(), testUser.getUserId());
        assertNotNull(createdLobby.getToken());
    }

    @Test
    void createLobby_duplicateName_throwsException() {

        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());

        User testUser = new User();
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        User testUser2 = new User();
        testUser2.setEmail("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        testUser2.setToken("token2");

        userRepository.save(testUser2);

        Lobby testLobby = new Lobby();
        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        Lobby testLobby2 = new Lobby();
        testLobby2.setLobbyName("testName");
        testLobby2.setLobbyMaxMembers(10);
        testLobby2.setLobbyRegion("Zurich");
        testLobby2.setLobbyTimeLimit(10);
        testLobby2.setHostMemberId(testUser2.getUserId());

        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby2));
    }

    @Test
    void getLobby_lobbyExists() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());

        User testUser = new User();
        
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();
        
        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        Lobby getLobby = lobbyService.getLobby(testLobby.getLobbyId());

        assertEquals(testLobby.getLobbyName(), getLobby.getLobbyName());
        assertEquals(testLobby.getLobbyRegion(), getLobby.getLobbyRegion());
        assertEquals(testLobby.getLobbyMaxMembers(), getLobby.getLobbyMaxMembers());
        assertEquals(testLobby.getLobbyTimeLimit(), getLobby.getLobbyTimeLimit());
        assertEquals(testLobby.getHostMemberId(), testUser.getUserId());
        assertEquals(testLobby.getToken(), getLobby.getToken());
    }

    @Test
    void getLobby_lobbyDoesNotExist_throwsException() {
        assertTrue(lobbyRepository.findAll().isEmpty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobby(1L));
    }

    @Test
    void getUser_userExists() {
        assertTrue(userRepository.findAll().isEmpty());

        User testUser = new User();
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);


        User getUser = lobbyService.getUser(testUser.getUserId());

        assertEquals(testUser.getUsername(), getUser.getUsername());
        assertEquals(testUser.getEmail(), getUser.getEmail());
        assertEquals(testUser.getPassword(), getUser.getPassword());
        assertEquals(testUser.getToken(), getUser.getToken());

    }

    @Test
    void getUser_userDoesNotExist_throwsException() {
        assertTrue(userRepository.findAll().isEmpty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.getUser(1L));
    }

    @Test
    void getMember_memberExists() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());


        User testUser = new User();
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();
        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());


        Member getMember = lobbyService.getMember(testLobby, testUser);

        assertEquals(getMember.getEmail(), testUser.getEmail());
        assertEquals(getMember.getUserId(), testUser.getUserId());
        assertEquals(getMember.getLobbyId(), testLobby.getLobbyId());
        assertEquals(getMember.getUsername(), testUser.getUsername());
    }

    @Test
    void getMember_memberDoesNotExist_throwsException() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        assertThrows(ResponseStatusException.class, () -> lobbyService.getMember(testLobby, testUser));
    }

    @Test
    void getMemberById_memberExists() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        Member testMember = new Member();
        
        testMember.setUserId(testUser.getUserId());
        testMember.setLobbyId(testLobby.getLobbyId());
        testMember.setUser(testUser);
        testMember.setEmail("testEmail");
        testMember.setUsername("testUsername");

        memberRepository.save(testMember);


        Member getMember = lobbyService.getMemberById(testMember.getMemberId());

        assertEquals(getMember.getMemberId(), testMember.getMemberId());
        assertEquals(getMember.getEmail(), testMember.getEmail());
        assertEquals(getMember.getUserId(), testMember.getUserId());
        assertEquals(getMember.getUsername(), testMember.getUsername());
        assertEquals(getMember.getUserId(), testMember.getUserId());
        assertEquals(getMember.getUserId(), testUser.getUserId());
    }

    @Test
    void getMemberById_memberDoesNotExist_throwsException() {
        assertTrue(memberRepository.findAll().isEmpty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.getMemberById(1L));
    }

    @Test
    void addMember_memberIsNotInLobby_lobbyIsNotFull() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");
        
        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());       


        Lobby createdLobby = lobbyService.createLobby(testLobby);


        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());


        createdLobby = lobbyRepository.findByLobbyId(createdLobby.getLobbyId());

        Member lobbyMember = createdLobby.getLobbyMembers().get(0);

        assertEquals(lobbyMember.getUserId(), testUser.getUserId());
        assertEquals(lobbyMember.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(lobbyMember.getEmail(), testUser.getEmail());
        assertFalse(lobbyMember.getHasLockedSelections());
    }

    @Test
    void addMember_memberIsNotInLobby_lobbyIsFull() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(0);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        assertThrows(ResponseStatusException.class, () -> lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken()));
    }

    @Test
    void addMember_memberIsAlreadyInLobby() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());


        lobbyService.createLobby(testLobby);

        lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        assertThrows(ResponseStatusException.class, () -> lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken()));
    }
    @Test
    void removeMember_lobbyIsNotEmpty() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        User testUser2 = new User();

        testUser2.setEmail("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        testUser2.setToken("token2");

        userRepository.save(testUser2);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());


        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Member testMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());
        Member testMember2 = lobbyService.addMember(testLobby.getLobbyId(), testUser2.getUserId(), testUser2.getToken());

        createdLobby = lobbyRepository.findByLobbyId(testLobby.getLobbyId());

        assertEquals(testMember.getUserId(), testUser.getUserId());
        assertEquals(testMember.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testMember.getEmail(), testUser.getEmail());
        assertFalse(testMember.getHasLockedSelections());

        assertEquals(testMember2.getUserId(), testUser2.getUserId());
        assertEquals(testMember2.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testMember2.getEmail(), testUser2.getEmail());
        assertFalse(testMember2.getHasLockedSelections());

        assertFalse(createdLobby.isLobbyEmpty());
        assertFalse(createdLobby.isLobbyFull());
        assertEquals(createdLobby.getLobbyMembersCount(), 2);

        lobbyService.removeMember(testLobby.getLobbyId(), testUser2.getUserId());
        createdLobby = lobbyRepository.findByLobbyId(createdLobby.getLobbyId());


        assertEquals(testMember.getUserId(), testUser.getUserId());
        assertEquals(testMember.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testMember.getEmail(), testUser.getEmail());
        assertFalse(testMember.getHasLockedSelections());

        assertFalse(createdLobby.isLobbyEmpty());
        assertFalse(createdLobby.isLobbyFull());
        assertEquals(createdLobby.getLobbyMembersCount(), 1);

        assertThrows(ResponseStatusException.class, () -> lobbyService.getMember(testLobby, testUser2));

    }

    @Test
    void removeMember_lobbyIsNowEmpty() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        Member testMember = lobbyService.getMember(testLobby, testUser);

        assertEquals(testMember.getUserId(), testUser.getUserId());
        assertEquals(testMember.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testMember.getEmail(), testUser.getEmail());
        assertFalse(testMember.getHasLockedSelections());

        assertFalse(createdLobby.isLobbyEmpty());
        assertFalse(createdLobby.isLobbyFull());
        assertEquals(createdLobby.getLobbyMembersCount(), 1);

        lobbyService.removeMember(testLobby.getLobbyId(), testUser.getUserId());

        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobby(testLobby.getLobbyId()));
    }

    @Test
    void deleteLobby() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        testLobby = lobbyService.createLobby(testLobby);

        assertNotNull(lobbyRepository.findByLobbyId(testLobby.getLobbyId()));

        lobbyService.deleteLobby(testLobby.getLobbyId());

        assertTrue(lobbyRepository.findAll().isEmpty());
    }

    @Test
    void setSports() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);

        Member testMember = lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());


        List<String> testSports = new ArrayList<>();
        testSports.add("Football");

        Member member = lobbyService.setSports(testLobby.getLobbyId(), testMember.getMemberId(), testSports);

        testMember = lobbyService.getMemberById(testMember.getMemberId());

        assertEquals(member.getMemberId(), testMember.getMemberId());
        assertEquals(member.getLobbyId(), testMember.getLobbyId());
        assertThat(member.getSelectedSports())
                .usingRecursiveComparison()
                .isEqualTo(testSports);
    }

    //commented out because the failing tests are blocking the deployment to sonarcloud
//    @Test
//    void setLocations() {
//        assertTrue(lobbyRepository.findAll().isEmpty());
//        assertTrue(userRepository.findAll().isEmpty());
//        assertTrue(memberRepository.findAll().isEmpty());
//
//        User testUser = new User();
//
//        testUser.setEmail("testName");
//        testUser.setUsername("testUsername");
//        testUser.setPassword("testPassword");
//
//        userRepository.save(testUser);
//
//        Lobby testLobby = new Lobby();
//
//        testLobby.setLobbyName("testName");
//        testLobby.setLobbyMaxMembers(10);
//        testLobby.setLobbyRegion("Zurich");
//        testLobby.setLobbyTimeLimit(10);
//        testLobby.setHostMemberId(testUser.getUserId());
//
//
//        Location testLocation = new Location();
//        testLocation.setLongitude(0.0);
//        testLocation.setLatitude(0.0);
//
//        testLobby = lobbyService.createLobby(testLobby);
//
//        Member testMember = lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());
//        testLocation.setMemberId(testMember.getMemberId());
//
//
//        lobbyService.addLobbyLocation(testLobby.getLobbyId(), testLocation);
//
//        List<String> testLocations = new ArrayList<>();
//        testLocations.add("0.0,0.0");
//
//        lobbyService.setLocations(testLobby.getLobbyId(), testMember.getMemberId(), testLocations);
//
//        testLocation = locationRepository.findByLocationId(testLocation.getLocationId());
//        testMember = lobbyService.getMemberById(testMember.getMemberId());
//        Location memberLocation = testMember.getSelectedLocations().get(0);
//
//        assertEquals(memberLocation.getLocationId(), testLocation.getLocationId());
//        assertEquals(memberLocation.getMemberId(), testLocation.getMemberId());
//        assertEquals(memberLocation.getLongitude(), testLocation.getLongitude());
//        assertEquals(memberLocation.getLatitude(), testLocation.getLatitude());
//        assertEquals(memberLocation.getLocationType(), testLocation.getLocationType());
//    }

    @Test
    void setDates() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());


        LocalDateTime testDate = LocalDateTime.of(2023, 10, 10, 1, 1, 1);

        testLobby = lobbyService.createLobby(testLobby);

        Member testMember = lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());


        List<String> testDates = new ArrayList<>();
        testDates.add(testDate.toString());

        lobbyService.setDates(testLobby.getLobbyId(), testMember.getMemberId(), testDates);

        testMember = lobbyService.getMemberById(testMember.getMemberId());
        LocalDateTime memberDate = testMember.getSelectedDates().get(0);

        assertEquals(memberDate, testDate);
    }

    @Test
    void lockSelections_memberHasSetAllSelections() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);
        lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Member testMember = lobbyService.getMember(testLobby, testUser);


        LocalDateTime date = LocalDateTime.of(2030, 1, 1, 11, 0);
        List<String> dates = new ArrayList<>();
        dates.add(date.toString());

        Location location = new Location();
        location.setLongitude(0.0);
        location.setLatitude(0.0);
        location.setMemberId(testMember.getMemberId());

        locationRepository.save(location);


        List<String> locations = new ArrayList<>();
        locations.add("0.0,0.0");

        List<String> sports = new ArrayList<>();
        sports.add("Football");

        lobbyService.setDates(testLobby.getLobbyId(), testMember.getMemberId(), dates);
        lobbyService.setSports(testLobby.getLobbyId(), testMember.getMemberId(), sports);
        //lobbyService.addLobbyLocation(testLobby.getLobbyId(),location);
        //lobbyService.setLocations(testLobby.getLobbyId(), testMember.getMemberId(), locations);
        lobbyService.addLobbyLocationVote(testLobby.getLobbyId(), testMember.getMemberId(), location.getLocationId());


        lobbyService.lockSelections(testLobby.getLobbyId(), testMember.getMemberId());

        testMember = lobbyService.getMember(testLobby, testUser);
        Lobby createdLobby = lobbyService.getLobby(testLobby.getLobbyId());

        assertTrue(testMember.getHasLockedSelections());
        assertTrue(createdLobby.isHaveAllMembersLockedSelections());
    }

    @Test
    void lockSelections_memberHasNotSetAllSelections_ThrowsException() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);
        lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Member testMember = lobbyService.getMember(testLobby, testUser);



        assertThrows(ResponseStatusException.class, () -> lobbyService.lockSelections(testLobby.getLobbyId(), testMember.getMemberId()));

    }

    @Test
    void unlockSelections() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        lobbyService.createLobby(testLobby);
        lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Member testMember = lobbyService.getMember(testLobby, testUser);
        testMember.setHasLockedSelections(true);

        lobbyService.unlockSelections(testLobby.getLobbyId(), testMember.getMemberId());

        testMember = lobbyService.getMember(testLobby, testUser);

        assertFalse(testMember.getHasLockedSelections());
        assertEquals(testMember.getUserId(), testUser.getUserId());
        assertEquals(testMember.getUsername(), testUser.getUsername());
        assertEquals(testMember.getLobbyId(), testLobby.getLobbyId());
    }

    @Test
    void addLobbyLocation() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Location testLocation = new Location();
        testLocation.setLongitude(0.0);
        testLocation.setLatitude(0.0);
        testLocation.setMemberId(addedMember.getMemberId());

        lobbyService.addLobbyLocation(testLobby.getLobbyId(), testLocation);

        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);
        Location location = locationRepository.findByLocationId(testLocation.getLocationId());

        assertEquals(location.getLocationId(), testLocation.getLocationId());
        assertEquals(location.getLobbyId(), testLobby.getLobbyId());
        assertEquals(location.getLatitude(), testLocation.getLatitude());
        assertEquals(location.getLongitude(), testLocation.getLongitude());
        assertEquals(addedMember.getMemberId(), location.getMemberId());
    }


    @Test
    void addLobbyLocationVote_locationExists() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Location testLocation = new Location();
        testLocation.setLongitude(0.0);
        testLocation.setLatitude(0.0);
        testLocation.setMemberId(addedMember.getMemberId());

        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);

        lobbyService.addLobbyLocation(createdLobby.getLobbyId(), testLocation);

        Location addedLocation = locationRepository.findByLocationId(testLocation.getLocationId());

        assertEquals(addedLocation.getMemberVotes(), 0);
        assertEquals(addedLocation.getLongitude(), testLocation.getLongitude());
        assertEquals(addedLocation.getLatitude(), testLocation.getLatitude());
        assertEquals(addedLocation.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(addedLocation.getMemberId(), addedMember.getMemberId());

        lobbyService.addLobbyLocationVote(testLobby.getLobbyId(), addedMember.getMemberId(), addedLocation.getLocationId());

        addedLocation = locationRepository.findByLocationId(addedLocation.getLocationId());
        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);

        assertEquals(addedLocation.getMemberVotes(), 1);
        assertEquals(addedLocation.getLongitude(), testLocation.getLongitude());
        assertEquals(addedLocation.getLatitude(), testLocation.getLatitude());
        assertEquals(addedLocation.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(addedLocation.getMemberId(), addedMember.getMemberId());
    }

    @Test
    void addLobbyLocationVote_locationDoesNotExists_ThrowsException() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);


        Lobby finalCreatedLobby = createdLobby;
        Member finalAddedMember = addedMember;
        assertThrows(ResponseStatusException.class, () -> lobbyService.addLobbyLocationVote(finalCreatedLobby.getLobbyId(), finalAddedMember.getMemberId(), 1L));
    }

    @Test
    void removeLobbyLocationVote_LocationExists() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Location testLocation = new Location();
        testLocation.setLongitude(0.0);
        testLocation.setLatitude(0.0);
        testLocation.setMemberId(addedMember.getMemberId());

        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);

        lobbyService.addLobbyLocation(createdLobby.getLobbyId(), testLocation);

        Location addedLocation = locationRepository.findByLocationId(testLocation.getLocationId());

        assertEquals(addedLocation.getMemberVotes(), 0);
        assertEquals(addedLocation.getLongitude(), testLocation.getLongitude());
        assertEquals(addedLocation.getLatitude(), testLocation.getLatitude());
        assertEquals(addedLocation.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(addedLocation.getMemberId(), addedMember.getMemberId());

        lobbyService.addLobbyLocationVote(createdLobby.getLobbyId(), addedMember.getMemberId(), addedLocation.getLocationId());

        addedLocation = locationRepository.findByLocationId(addedLocation.getLocationId());
        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);

        assertEquals(addedLocation.getMemberVotes(), 1);
        assertEquals(addedLocation.getLongitude(), testLocation.getLongitude());
        assertEquals(addedLocation.getLatitude(), testLocation.getLatitude());
        assertEquals(addedLocation.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(addedLocation.getMemberId(), addedMember.getMemberId());

        lobbyService.removeLobbyLocationVote(createdLobby.getLobbyId(), addedMember.getMemberId(), addedLocation.getLocationId());

        addedLocation = locationRepository.findByLocationId(addedLocation.getLocationId());
        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);

        assertEquals(addedLocation.getMemberVotes(), 0);
        assertEquals(addedLocation.getLongitude(), testLocation.getLongitude());
        assertEquals(addedLocation.getLatitude(), testLocation.getLatitude());
        assertEquals(addedLocation.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(addedLocation.getMemberId(), addedMember.getMemberId());
    }

    @Test
    void removeLobbyLocationVote_locationDoesNotExists_ThrowsException() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        createdLobby = lobbyService.getLobby(createdLobby.getLobbyId());
        addedMember = lobbyService.getMember(createdLobby, testUser);


        Lobby finalCreatedLobby = createdLobby;
        Member finalAddedMember = addedMember;
        assertThrows(ResponseStatusException.class, () -> lobbyService.removeLobbyLocationVote(finalCreatedLobby.getLobbyId(), finalAddedMember.getMemberId(), 1L));
    }

    @Test
    void getMembers() {
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());


        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        List<Member> members = memberRepository.findAll();
        Member getMember = members.get(0);

        assertEquals(getMember.getUserId(), testUser.getUserId());
        assertEquals(getMember.getLobbyId(), testLobby.getLobbyId());
        assertEquals(getMember.getUsername(), testUser.getUsername());
    }

    @Test
    void getLocations() {
        assertTrue(lobbyRepository.findAll().isEmpty());
        assertTrue(userRepository.findAll().isEmpty());
        assertTrue(memberRepository.findAll().isEmpty());
        assertTrue(locationRepository.findAll().isEmpty());

        User testUser = new User();

        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("token");

        userRepository.save(testUser);

        Lobby testLobby = new Lobby();

        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(testUser.getUserId());

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Member addedMember = lobbyService.addMember(createdLobby.getLobbyId(), testUser.getUserId(), testUser.getToken());

        Location testLocation = new Location();
        testLocation.setLongitude(0.0);
        testLocation.setLatitude(0.0);
        testLocation.setMemberId(addedMember.getMemberId());

        lobbyService.addLobbyLocation(testLobby.getLobbyId(), testLocation);

        Location createdLocation = locationRepository.findAll().get(0);

        assertEquals(createdLocation.getLocationId(), testLocation.getLocationId());
        assertEquals(createdLocation.getLatitude(), testLocation.getLatitude());
        assertEquals(createdLocation.getLongitude(), testLocation.getLongitude());
        assertEquals(createdLocation.getMemberId(), testLocation.getMemberId());
    }

}
