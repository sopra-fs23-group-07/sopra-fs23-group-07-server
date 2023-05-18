package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.entity.Timer;
import ch.uzh.ifi.hase.soprafs23.repository.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MessageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.util.UserUtil;
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
    private final ParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final UserUtil userUtil;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                        @Qualifier("userRepository") UserRepository userRepository,
                        @Qualifier("memberRepository") MemberRepository memberRepository,
                        @Qualifier("locationRepository") LocationRepository locationRepository,
                        @Qualifier("eventRepository") EventRepository eventRepository,
                        @Qualifier("timerRepository") TimerRepository timerRepository,
                        @Qualifier("participantRepository") ParticipantRepository participantRepository,
                        @Qualifier("messageRepository") MessageRepository messageRepository,
                        UserUtil userUtil) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.timerRepository = timerRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
        this.userUtil = userUtil;
    }

    public LobbyGetDTO updateLobby(Lobby lobby) {
        lobby.setLobbyDecidedSport(lobby.decideSport());
        lobby.decideLocation();
        lobby.setLobbyDecidedDate(lobby.decideDate());

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        if(lobby.isAtLeastTwoMembersHaveLockedSelections() && (lobby.isHaveAllMembersLockedSelections() || lobby.hasTimerRunOut())) {

            if(lobby.getCreatedEventId() == null) {

                Event event = lobby.createEvent();
                eventRepository.save(event);

                for(Member member : lobby.getLobbyMembers()) {

                    User databaseUser = userRepository.findByUserId(member.getUserId());
                    Participant participant = new Participant();
                    participant.setUser(databaseUser);
                    participant.setEventId(event.getEventId());
                    participant.setEvent(event);

                    event.addEventParticipant(participant);
                    event.addEventUser(databaseUser);
                    databaseUser.addEvent(event);
                    participantRepository.save(participant);
                    userRepository.save(databaseUser);
                    eventRepository.save(event);
                }

                event.getEventLocation().setEventId(event.getEventId());

                event = eventRepository.save(event);
                eventRepository.flush();

                lobby.setCreatedEventId(event.getEventId());
                lobby = lobbyRepository.save(lobby);
                lobbyRepository.flush();

                log.debug("Created Information for Event: {}", event);
            }
            lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        }
        return lobbyGetDTO;
    }

    public void addLobbyMessage(Long lobbyId, Long userId, MessageDTO messageDTO) {
        Lobby lobby = getLobby(lobbyId);
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s was not found"
                    , messageDTO.getUsername()));
        }

        getMember(lobby, user);

        Message message = new Message();

        message.setUsername(user.getUsername());
        message.setLobbyMessage(messageDTO.getMessage());
        message.setLobbyId(lobbyId);

        messageRepository.save(message);
        messageRepository.flush();

        lobby.addMessageToLobbyChat(message);

        lobbyRepository.save(lobby);
        lobbyRepository.flush();
    }

    public Lobby createLobby(Lobby newLobby) {
        checkIfLobbyExists(newLobby);
        if (newLobby.getLobbyMaxMembers() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum number of lobby members cannot exceed 30 people.");
        }
        //checkIfUserIsMemberOfALobby(userRepository.findByUserId(newLobby.getHostMemberId())); //restriction to be member of only 1 lobby
        newLobby.setToken(UUID.randomUUID().toString());

        // Set the lobby timer and save it to the database
        Timer timer = new Timer();
        timer.setStartTime(LocalDateTime.now());
        timer.setLobby(newLobby);
        this.timerRepository.save(timer);

        // Set the timer on the lobby and return it
        newLobby.setTimer(timer);

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

    public Member addMember(Long lobbyId, Long userId, String token) {
        Lobby lobby = getLobby(lobbyId);
        User databaseUser = userUtil.getUser(userId, token);
        //checkIfUserIsMemberOfALobby(databaseUser); //restriction to be member of only 1 lobby
        if (lobby.isLobbyFull()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby is full");
        }
        if (memberRepository.findByLobbyAndUser(lobby, databaseUser).isPresent()) {
            String baseErrorMessage = "The %s provided %s already member of this lobby";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "userId", "is"));
        }
        Member member = new Member();
        member.setUser(databaseUser);
        member.setLobbyId(lobbyId);

        lobby.addLobbyMember(member);
        lobby.addLobbyUser(databaseUser);
        databaseUser.addLobby(lobby);
        memberRepository.save(member);
        userRepository.save(databaseUser);
        lobbyRepository.save(lobby);
        return member;
    }

    public void removeMember(Long lobbyId, Long userId, String token) {
        Lobby lobby = getLobby(lobbyId);
        User databaseUser = userUtil.getUser(userId, token);
        Member member = getMember(lobby, databaseUser);

        for (Location location : member.getSelectedLocations()) {
            location.getSelectedMembers().remove(member);
            location.removeMemberVotes(member.getMemberId());
        }
        lobby.removeLobbyLocation(member.getSuggestedLocation());

        lobby.removeLobbyMember(member);
        lobby.removeLobbyUser(databaseUser);
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
        checkIfIsMemberOfLobby(lobby, member);
        member.setSelectedSports(selectedSports);

        return member;
    }

    public Member setDates(Long lobbyId, Long memberId, List<String> selectedDates) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(memberId);
        checkIfIsMemberOfLobby(lobby, member);
        List<LocalDateTime> dates = new ArrayList<>();
        for (String string : selectedDates) {
            if (!LocalDateTime.parse(string).isBefore(LocalDateTime.now())) {
                dates.add(LocalDateTime.parse(string));
            }
        }
        member.setSelectedDates(dates);
        return member;
    }
    public Member lockSelections(Long lobbyId, Long memberId) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(memberId);
        checkIfIsMemberOfLobby(lobby, member);
        String errorMessage = "";
        if (member.getSelectedSports().isEmpty()) {
            errorMessage += "Please select at least one sport.\n";
        }
        if (member.getSelectedDates().isEmpty()) {
            errorMessage += "Please select at least one date.\n";
        }
        if (member.getSelectedLocations().isEmpty()) {
            errorMessage += "Please vote for at least one location." +
                    " If there are no locations to vote for, please confirm a location and vote for it.";
        }
        if (!errorMessage.equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            member.setHasLockedSelections(true);
            /**if (lobby.getLobbyMembers().size() == 1) {
                throw new ResponseStatusException(HttpStatus.OK, "Event will only be created if at least two users locked their choices");
            }**/
        }
        return member;
    }

    public Member unlockSelections(Long lobbyId, Long memberId) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(memberId);
        checkIfIsMemberOfLobby(lobby, member);
        member.setHasLockedSelections(false);
        return member;
    }
    public void addLobbyLocation (Long lobbyId, Location location) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(location.getMemberId());
        checkIfIsMemberOfLobby(lobby, member);
        if (member.getSuggestedLocation() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Member with memberId %s has already" +
                    " posted a location", member.getMemberId()));
        }
        location.setLobbyId(lobbyId);
        //location.setMember(member);
        member.setSuggestedLocation(location);

        locationRepository.save(location);
        lobby.addLobbyLocation(location);
        lobbyRepository.save(lobby);
    }
    public void removeLobbyLocation(Long lobbyId, Long memberId) {
        Lobby lobby = getLobby(lobbyId);
        Member member = getMemberById(memberId);
        checkIfIsMemberOfLobby(lobby, member);
        if (member.getSuggestedLocation() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member with memberId %s has not yet" +
                    " posted a location", member.getMemberId()));
        }
        lobby.removeLobbyLocation(member.getSuggestedLocation());
        member.setSuggestedLocation(null);
    }
    public void addLobbyLocationVote(Long lobbyId, Long memberId, Long locationId) {
        Location location = getLocation(locationId);
        Member member = getMemberById(memberId);
        Lobby lobby = getLobby(lobbyId);
        checkIfIsMemberOfLobby(lobby, member);
        //member.setLocationId(locationId);
        location.getSelectedMembers().add(member);
        //location.setMemberId(memberId);
        location.addMemberVotes(memberId);
        member.addSelectedLocation(location);

        locationRepository.save(location);
        lobbyRepository.save(lobby);
        memberRepository.save(member);
    }
    public void removeLobbyLocationVote(Long lobbyId, Long memberId, Long locationId) {
        Location location = getLocation(locationId);
        Member member = getMemberById(memberId);
        Lobby lobby = getLobby(lobbyId);
        checkIfIsMemberOfLobby(lobby, member);
        location.getSelectedMembers().remove(member);
        location.removeMemberVotes(memberId);
        member.removeSelectedLocation(location);

        locationRepository.save(location);
        lobbyRepository.save(lobby);
        memberRepository.save(member);
    }
    private Location getLocation(Long locationId) {
        Location location = locationRepository.findByLocationId(locationId);
        if (location == null) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "locationId", "was"));
        }
        return location;
    }
    private void checkIfIsMemberOfLobby(Lobby lobby, Member member) {
        List<Member> members = lobby.getLobbyMembers();
        if (!members.contains(member)) {
            String baseErrorMessage = "The %s provided %s not member of Lobby with LobbyId %s";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "memberId", "is",
                    lobby.getLobbyId()));
        }
    }
    private void checkIfTimerHasRunUp(Lobby lobby) {
        if (lobby.hasTimerRunOut()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Timer has expired");
        }
    }
    public List<Lobby> getLobbies() {
        List<Lobby> lobbies = this.lobbyRepository.findAll();
        for (Lobby lobby: lobbies) {
            if (lobby.getTimeRemaining() == -1) {
                lobbyRepository.delete(lobby);
            }
        }
        return lobbies;
    }
}