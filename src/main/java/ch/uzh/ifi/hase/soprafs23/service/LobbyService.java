package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.entity.Timer;
import ch.uzh.ifi.hase.soprafs23.repository.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Lobby Service
 * This class is the "worker" and responsible for all functionality related to
 * the lobby
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final TimerRepository timerRepository;

    //private final EventService eventService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                        @Qualifier("userRepository") UserRepository userRepository,
                        @Qualifier("memberRepository") MemberRepository memberRepository,
                        @Qualifier("locationRepository") LocationRepository locationRepository,
                        @Qualifier("eventRepository") EventRepository eventRepository,
                        @Qualifier("timerRepository") TimerRepository timerRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.timerRepository = timerRepository;
    }


    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public LobbyGetDTO updateLobby(Lobby lobby) {
        lobby.setLobbyDecidedSport(lobby.decideSport());
        lobby.setLobbyDecidedLocation(lobby.decideLocation());
        lobby.setLobbyDecidedDate(lobby.decideDate());

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        if(lobby.isHaveAllMembersLockedSelections() || lobby.hasTimerRunOut()) {
            Event event = checkIfEventExists(lobby.getCreatedEventId());

            if(event == null) {

                event = lobby.createEvent();
                event = eventRepository.save(event);
                eventRepository.flush();

                lobby.setCreatedEventId(event.getEventId());

                for(Member member : lobby.getLobbyMembers()) {
                    memberRepository.delete(member);
                }
                timerRepository.delete(lobby.getTimer());
                lobbyRepository.delete(lobby);

                log.debug("Created Information for Event: {}", event);
            }

            lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
            //lobbyGetDTO.setCreatedEventId(event.getEventId());


        }
        return lobbyGetDTO;
    }

    private Event checkIfEventExists(Long eventId) {
        Optional<Event> eventById = eventRepository.findById(eventId);

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the lobby could not be created!";
        if (eventById.isEmpty()) {
            return null;
        }
        return eventById.get();
    }

    public Lobby createLobby(Lobby newLobby) {
        checkIfLobbyExists(newLobby);
        newLobby.setToken(UUID.randomUUID().toString());

        // Set the lobby timer and save it to the database
        Timer timer = new Timer();
        timer.setStartTime(LocalDateTime.now());
        timer.setLobby(newLobby);
        this.timerRepository.save(timer);

        // Set the timer on the lobby and return it
        newLobby.setTimer(timer);

        //Member hostMember = new Member(hostUser);
        //newLobby.addLobbyMember(hostMember);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }
    private void checkIfLobbyExists(Lobby lobbyToBeCreated) {
        Lobby lobbyByLobbyName = lobbyRepository.findByLobbyName(lobbyToBeCreated.getLobbyName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the lobby could not be created!";
        if (lobbyByLobbyName != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "lobbyName", "is"));
        }
    }
    public Lobby getLobby(Long lobbyId) {
        Lobby lobbyToFind = lobbyRepository.findByLobbyId(lobbyId);
        if (lobbyToFind == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "lobbyId", "was"));
        }
        return lobbyToFind;
    }
    public User getUser(Long userId) {
        User userToFind = userRepository.findByUserId(userId);
        if (userToFind == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "userId", "was"));
        }
        return userToFind;
    }
    public Member getMember(Lobby lobby, User user) {
        Optional<Member> memberToFind = memberRepository.findByLobbyAndUser(lobby, user);
        if(memberToFind.isEmpty()) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "memberId", "was"));
        }
        return memberToFind.get();
    }
    public Member getMemberById(Long memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "memberId", "was"));
        }
        return member.get();
    }

    public Member addMember(Long lobbyId, Long userId) {
        Lobby lobby = getLobby(lobbyId);
        User databaseUser = getUser(userId);
        if (lobby.isLobbyFull()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby is full");
        }
        if (memberRepository.findByLobbyAndUser(lobby, databaseUser).isPresent()) {
            String baseErrorMessage = "The %s provided %s already member of this lobby";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "userId", "is"));
        }
        Member member = new Member();
        member.setUser(databaseUser);
        member.setLobbyId(lobby.getLobbyId());

        lobby.addLobbyMember(member);
        databaseUser.addLobby(lobby);
        memberRepository.save(member);
        userRepository.save(databaseUser);
        lobbyRepository.save(lobby);
        return member;
    }
    public void removeMember(Long lobbyId, Long userId) {
        Lobby lobby = getLobby(lobbyId);
        User databaseUser = getUser(userId);
        Member member = getMember(lobby, databaseUser);

        lobby.removeLobbyMember(member);
        databaseUser.removeLobby(lobby);
        //databaseUser.removeMember(member);
        memberRepository.delete(member);
        lobbyRepository.save(lobby);
        userRepository.save(databaseUser);

        if(lobby.getLobbyMembersCount() == 0) {lobbyRepository.delete(lobby);}
    }
    public void deleteLobby(Long lobbyId) {
        Lobby lobby = getLobby(lobbyId);
        lobbyRepository.delete(lobby);
    }
    public Member setSports(Long lobbyId, Long memberId, List<String> selectedSports) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(memberId);
        member.setSelectedSports(selectedSports);
        //member.addSelectedSport(selectedSport);

        return member;
    }
    //to remove
    public void setLocations(Long lobbyId, Long memberId, List<String> selectedLocations) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(memberId);
        List<Location> locations = new ArrayList<>();
        for (String string : selectedLocations) {
            String[] coordinates = string.split(",");
            String longitudeString = coordinates[0];
            String latitudeString = coordinates[1];

            double longitude = Double.parseDouble(longitudeString);
            double latitude = Double.parseDouble(latitudeString);

            Location location = new Location();
            location.setLongitude(longitude);
            location.setLatitude(latitude);
            location.setAddress(string);

            locations.add(location);
            locationRepository.save(location);
        }
        member.setSelectedLocations(locations);
    }
    public void setDates(Long lobbyId, Long memberId, List<String> selectedDates) {
        Member member = getMemberById(memberId);
        List<LocalDateTime> dates = new ArrayList<>();
        for (String string : selectedDates) {
            dates.add(LocalDateTime.parse(string));
        }
        member.setSelectedDates(dates);
    }
    public Member lockSelections(Long lobbyId, Long memberId) {
        Member member = getMemberById(memberId);
        member.setHasLockedSelections(true);
        return member;
    }

    public Member unlockSelections(Long lobbyId, Long memberId) {
        Member member = getMemberById(memberId);
        member.setHasLockedSelections(false);
        return member;
    }
    public void addLobbyLocation (Long lobbyId, Location location) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(location.getMemberId());
        checkIfIsMemberOfLobby(lobby, member);

        location.setLobbyId(lobbyId);

        locationRepository.save(location);
        lobby.addLobbyLocation(location);
        lobbyRepository.save(lobby);
    }
    public void addLobbyLocationVote(Long lobbyId, Long memberId, Long locationId) {
        Location location = locationRepository.findByLocationId(locationId);
        if (location == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "locationId", "was"));
        }
        Member member = getMemberById(memberId);
        Lobby lobby = getLobby(lobbyId);
        checkIfIsMemberOfLobby(lobby, member);
        location.addMemberVotes(memberId);
        //member.setSelectedLocations(location.get());

        locationRepository.save(location);
        lobbyRepository.save(lobby);
        memberRepository.save(member);
    }
    public void removeLobbyLocationVote(Long lobbyId, Long memberId, Long locationId) {
        Location location = locationRepository.findByLocationId(locationId);
        if (location == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "locationId", "was"));
        }
        Member member = getMemberById(memberId);
        Lobby lobby = getLobby(lobbyId);
        checkIfIsMemberOfLobby(lobby, member);
        location.removeMemberVotes(memberId);

        locationRepository.save(location);
        lobbyRepository.save(lobby);
        memberRepository.save(member);
    }
    private void checkIfIsMemberOfLobby(Lobby lobby, Member member) {
        List<Member> members = lobby.getLobbyMembers();
        if (!members.contains(member)) {
            String baseErrorMessage = "The %s provided %s not member of Lobby with LobbyId %s";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "memberId", "is",
                    lobby.getLobbyId()));
        }
    }

    public List<Member> getMembers() {
        return this.memberRepository.findAll();
    }
    public List<Location> getLocations() {
        return this.locationRepository.findAll();
    }

    public void stopTimer(Timer timer) {
        if (timer == null) {
            throw new RuntimeException("Timer is not started");
        }

        this.timerRepository.delete(timer);
        // Cancel the timer task using the Timer class as before
        // ...
    }
}