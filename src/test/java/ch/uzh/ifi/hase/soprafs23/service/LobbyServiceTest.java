package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.*;
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
import static org.mockito.Mockito.times;

// test for Sprint2
class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private TimerRepository timerRepository;
    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    LocationRepository locationRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private User testUser;
    private Lobby testLobby;

    private Member testMember;

    private Location testLocation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setEmail("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

        testLocation = new Location();
        testLocation.setLocationId(1L);
        //testLocation.setLobbyId(1L);
        testLocation.setLongitude(0.0);
        testLocation.setLatitude(0.0);
        testLocation.setAddress("Home");

        Mockito.when(locationRepository.save(Mockito.any())).thenReturn(testLocation);



        List<LocalDateTime> selectedDates = new ArrayList<>();
        selectedDates.add(LocalDateTime.now());

        List<Location> selectedLocations = new ArrayList<>();
        selectedLocations.add(testLocation);

        List<String> selectedSports = new ArrayList<>();
        selectedSports.add("Football");

        testMember = new Member();

        testMember.setMemberId(1L);
        testMember.setUserId(1L);
        testMember.setLobbyId(1L);
        testMember.setUser(testUser);
        testMember.setEmail("testEmail");
        testMember.setUsername("testUsername");
        testMember.setSelectedDates(selectedDates);
        testMember.setSelectedLocations(selectedLocations);
        testMember.setSelectedSports(selectedSports);


        Mockito.when(memberRepository.save(Mockito.any())).thenReturn(testMember);


        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyName("testName");
        testLobby.setLobbyMaxMembers(10);
        testLobby.setLobbyRegion("Zurich");
        testLobby.setLobbyTimeLimit(10);
        testLobby.setHostMemberId(1L);
        //testLobby.addLobbyMember(testMember);

        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);

    }


    @Test
    void createLobby_validInputs() {
        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        // then
        Mockito.verify(lobbyRepository, times(1)).save(Mockito.any());

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
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        lobbyService.createLobby(testLobby);

        // when -> setup additional mocks for UserRepository
        Mockito.when(lobbyRepository.findByLobbyName(Mockito.any())).thenReturn(testLobby);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby));
    }

    @Test
    void getLobby_lobbyExists() {


        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

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
    void getMember_memberExists() {
        Mockito.when(memberRepository.findByLobbyAndUser(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        Member getMember = lobbyService.getMember(testLobby, testUser);

        assertEquals(getMember.getMemberId(), testMember.getMemberId());
        assertEquals(getMember.getEmail(), testMember.getEmail());
        assertEquals(getMember.getUserId(), testMember.getUserId());
    }

    @Test
    public void getMember_memberDoesNotExist_throwsException() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.getMember(testLobby, testUser));
    }

    @Test
    void getMemberById_memberExists() {
        Mockito.when(memberRepository.findByMemberId(Mockito.anyLong())).thenReturn(Optional.ofNullable(testMember));

        Member getMember = lobbyService.getMemberById(testMember.getMemberId());

        assertEquals(getMember.getMemberId(), testMember.getMemberId());
        assertEquals(getMember.getEmail(), testMember.getEmail());
        assertEquals(getMember.getUserId(), testMember.getUserId());
    }

    @Test
    public void getMemberById_memberDoesNotExist_throwsException() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.getMemberById(testMember.getMemberId()));
    }

    @Test
    void addMember_memberIsNotInLobby_lobbyIsNotFull() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        Member addedMember = lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId());

        List<Member> testMembers = new ArrayList<>();
        testMembers.add(addedMember);

        assertEquals(addedMember.getUserId(), testUser.getUserId());
        assertEquals(addedMember.getLobbyId(), testLobby.getLobbyId());
        assertEquals(addedMember.getEmail(), testUser.getEmail());
        assertFalse(addedMember.getHasLockedSelections());
        assertEquals(testLobby.getLobbyMembers(), testMembers);
    }

    @Test
    void addMember_memberIsNotInLobby_lobbyIsFull() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        testLobby.setLobbyMaxMembers(0);

        assertThrows(ResponseStatusException.class, () -> lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId()));
    }

    @Test
    void addMember_memberIsAlreadyInLobby() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
        Mockito.when(memberRepository.findByLobbyAndUser(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        //testLobby.addLobbyMember(testMember);

        assertThrows(ResponseStatusException.class, () -> lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId()));
    }

    @Test
    void removeMember_lobbyIsNotEmpty() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
        testMember.setSelectedLocations(new ArrayList<>());
        Mockito.when(memberRepository.findByLobbyAndUser(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        testLobby.addLobbyMember(testMember);
        Member testMember2 = new Member();
        testLobby.addLobbyMember(testMember2);

        lobbyService.removeMember(testLobby.getLobbyId(), testUser.getUserId());

        List<Member> members = new ArrayList<>();
        members.add(testMember2);

        assertEquals(testLobby.getLobbyMembers(), members);
        assertFalse(testLobby.isLobbyEmpty());
        assertEquals(testLobby.getLobbyMembersCount(), 1);

    }

    @Test
    void removeMember_lobbyIsNowEmpty() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
        testMember.setSelectedLocations(new ArrayList<>());
        Mockito.when(memberRepository.findByLobbyAndUser(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        testLobby.addLobbyMember(testMember);

        lobbyService.removeMember(testLobby.getLobbyId(), testUser.getUserId());

        List<Member> members = new ArrayList<>();

        assertEquals(testLobby.getLobbyMembers(), members);
        assertTrue(testLobby.isLobbyEmpty());
        assertEquals(testLobby.getLobbyMembersCount(), 0);

    }

    @Test
    void deleteLobby() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        lobbyService.deleteLobby(testLobby.getLobbyId());

        Mockito.verify(lobbyRepository, times(1)).delete(testLobby);
    }

    @Test
    void setSports() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        List<String> testSports = new ArrayList<>();
        testSports.add("Football");

        testMember.setSelectedSports(new ArrayList<>());

        testLobby.addLobbyMember(testMember);

        Member member = lobbyService.setSports(testLobby.getLobbyId(), testMember.getMemberId(), testSports);

        assertEquals(member.getMemberId(), testMember.getMemberId());
        assertEquals(member.getLobbyId(), testMember.getLobbyId());
        assertEquals(member.getSelectedSports(), testMember.getSelectedSports());

    }

    @Test
    void setLocations() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        List<String> testLocations = new ArrayList<>();
        testLocations.add("0.0,0.0");

        testMember.setSelectedLocations(new ArrayList<>());

        testLobby.addLobbyMember(testMember);

        lobbyService.setLocations(testLobby.getLobbyId(), testMember.getMemberId(), testLocations);

        assertEquals(testMember.getSelectedLocations().get(0).getLatitude(), 0.0);
        assertEquals(testMember.getSelectedLocations().get(0).getLongitude(), 0.0);

    }

    @Test
    void setDates() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        List<String> testDates = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.of(2030, 1, 1, 11, 0, 0);
        testDates.add(currentDate.toString());

        testMember.setSelectedDates(new ArrayList<>());

        testLobby.addLobbyMember(testMember);

        lobbyService.setDates(testLobby.getLobbyId(), testMember.getMemberId(), testDates);

        List<LocalDateTime> addedDates = new ArrayList<>();
        addedDates.add(currentDate);

        assertEquals(testMember.getSelectedDates(), addedDates);
    }

    @Test
    void lockSelections() {
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        testLobby.addLobbyMember(testMember);

        Member member = lobbyService.lockSelections(testLobby.getLobbyId(), testMember.getMemberId());


        assertTrue(testMember.getHasLockedSelections());
        assertEquals(testMember.getMemberId(), member.getMemberId());
        assertEquals(testMember.getLobbyId(), member.getLobbyId());
    }

    @Test
    void unlockSelections() {
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);

        testLobby.addLobbyMember(testMember);

        Member member = lobbyService.unlockSelections(testLobby.getLobbyId(), testMember.getMemberId());

        assertFalse(testMember.getHasLockedSelections());
        assertEquals(testMember.getMemberId(), member.getMemberId());
        assertEquals(testMember.getLobbyId(), member.getLobbyId());
    }

    @Test
    void addLobbyLocation() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        testLobby.addLobbyMember(testMember);
        lobbyService.addLobbyLocation(testLobby.getLobbyId(), testLocation);

        List<Location> locations = new ArrayList<>();
        locations.add(testLocation);

        assertEquals(testLobby.getLobbyLocations(), locations);
    }
    @Test
    void removeLobbyLocation() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));

        testLobby.addLobbyMember(testMember);
        lobbyService.addLobbyLocation(testLobby.getLobbyId(), testLocation);

        List<Location> locations = new ArrayList<>();
        locations.add(testLocation);

        assertEquals(testLobby.getLobbyLocations(), locations);

        lobbyService.removeLobbyLocation(testLobby.getLobbyId(), testMember.getMemberId());

        assertTrue(testLobby.getLobbyLocations().isEmpty());
    }



    @Test
    void addLobbyLocationVote_locationExists() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        testMember.setSelectedLocations(new ArrayList<>());
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));
        Mockito.when(locationRepository.findByLocationId(Mockito.anyLong())).thenReturn(testLocation);

        testLobby.addLobbyMember(testMember);
        testLobby.addLobbyLocation(testLocation);

        lobbyService.addLobbyLocationVote(testLobby.getLobbyId(), testMember.getMemberId(), testLocation.getLocationId());

        Location location = testLobby.getLobbyLocations().get(0);
        assertEquals(location.getMemberVotes(), 1);

    }

    @Test
    void addLobbyLocationVote_locationDoesNotExists_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.addLobbyLocationVote(testLobby.getLobbyId(), testMember.getMemberId(), 1L));
    }


    @Test
    void removeLobbyLocationVote_LocationExists() {
        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(memberRepository.findByMemberId(Mockito.any())).thenReturn(Optional.ofNullable(testMember));
        Mockito.when(locationRepository.findByLocationId(Mockito.anyLong())).thenReturn(testLocation);

        testLobby.addLobbyMember(testMember);
        testLobby.addLobbyLocation(testLocation);
        testLocation.addMemberVotes(testMember.getMemberId());

        lobbyService.removeLobbyLocationVote(testLobby.getLobbyId(), testMember.getMemberId(), testLocation.getLocationId());

        assertEquals(testLocation.getMemberVotes(), 0);
    }

    @Test
    void removeLobbyLocationVote_locationDoesNotExists_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> lobbyService.removeLobbyLocationVote(testLobby.getLobbyId(), testMember.getMemberId(), 1L));
    }

    @Test
    void getMembers() {
        memberRepository.save(testMember);

        List<Member> testMembers = new ArrayList<>();
        testMembers.add(testMember);

        Mockito.when(memberRepository.findAll()).thenReturn(testMembers);

        List<Member> members = lobbyService.getMembers();


        assertEquals(members, testMembers);
    }

    @Test
    void getLocations() {
        locationRepository.save(testLocation);

        List<Location> testLocations = new ArrayList<>();
        testLocations.add(testLocation);

        Mockito.when(locationRepository.findAll()).thenReturn(testLocations);

        List<Location> locations = lobbyService.getLocations();


        assertEquals(locations, testLocations);
    }
    @Test
    void updateLobby() {
        User testUser2 = new User();
        testUser2.setUserId(1L);
        testUser2.setEmail("testName");
        testUser2.setUsername("testUsername");
        testUser2.setPassword("testPassword");

        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        createdLobby.setCreatedEventId(null);

        Mockito.when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        Member addedMember = lobbyService.addMember(testLobby.getLobbyId(), testUser.getUserId());
        Member addedMember2 = lobbyService.addMember(testLobby.getLobbyId(), testUser2.getUserId());

        addedMember.setSuggestedLocation(testLocation);
        addedMember.addSelectedLocation(testLocation);
        testLobby.addLobbyLocation(testLocation);
        addedMember.addSelectedSport("soccer");
        List<LocalDateTime> dates = new ArrayList<>();
        dates.add(LocalDateTime.of(2030, 1, 1, 11, 0, 0));
        addedMember.setSelectedDates(dates);
        addedMember.setHasLockedSelections(true);

        addedMember2.setSuggestedLocation(testLocation);
        addedMember2.addSelectedLocation(testLocation);
        addedMember2.addSelectedSport("soccer");
        List<LocalDateTime> dates2 = new ArrayList<>();
        dates.add(LocalDateTime.of(2030, 1, 1, 11, 0, 0));
        addedMember2.setSelectedDates(dates2);
        addedMember2.setHasLockedSelections(true);

        final Event[] testEvent = {new Event()};
        Participant testParticipant = new Participant();

        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(invocation -> {
            testEvent[0] = invocation.getArgument(0);
            return testEvent[0];
        });
        Mockito.when(participantRepository.save(Mockito.any(Participant.class))).thenReturn(testParticipant);

        LobbyGetDTO result = lobbyService.updateLobby(createdLobby);

        assertEquals("soccer", testLobby.getLobbyDecidedSport());
        assertEquals(LocalDateTime.of(2030, 1, 1, 11, 0, 0), testLobby.getLobbyDecidedDate());
        assertEquals(testLocation, testLobby.getDecidedLocation());

        assertEquals("soccer", testEvent[0].getEventSport());
        assertEquals(LocalDateTime.of(2030, 1, 1, 11, 0, 0), testEvent[0].getEventDate());
        assertEquals(testLocation.getAddress(), testEvent[0].getEventLocation().getAddress());

        assertEquals("testName", testEvent[0].getEventName());
        assertEquals(2, testEvent[0].getEventParticipants().size());
        assertEquals("Zurich", testEvent[0].getEventRegion());

        assertTrue(result.isHaveAllMembersLockedSelections());
    }
}