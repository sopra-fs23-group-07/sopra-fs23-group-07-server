package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MemberRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    //private final EventService eventService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                        @Qualifier("userRepository") UserRepository userRepository,
                        @Qualifier("memberRepository") MemberRepository memberRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }


    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public Lobby createLobby(Lobby newLobby) {
        newLobby.setToken(UUID.randomUUID().toString());

        //Member hostMember = new Member(hostUser);
        //newLobby.addLobbyMember(hostMember);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }
    public Lobby getLobby(Long lobbyId) {
        Optional<Lobby> lobbyToFind = lobbyRepository.findById(lobbyId);
        if (lobbyToFind.isEmpty()) {
            String baseErrorMessage = "The %s provided %s not found";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "eventId", "was"));
        }
        return lobbyToFind.get();
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

    public void addMember(Long lobbyId, Long userId) {
        Lobby lobby = getLobby(lobbyId);
        User databaseUser = getUser(userId);
        Member member = new Member();
        member.setUser(databaseUser);
        member.setLobbyId(lobby.getLobbyId());

        lobby.addLobbyMember(member);
        databaseUser.addLobby(lobby);
        memberRepository.save(member);
        userRepository.save(databaseUser);
        lobbyRepository.save(lobby);
    }
    public void removeMember(Long lobbyId, Long userId) {
        Lobby lobby = getLobby(lobbyId);
        User databaseUser = getUser(userId);
        Member member = getMember(lobby, databaseUser);

        lobby.removeLobbyMember(member);
        databaseUser.removeLobby(lobby);
        lobbyRepository.save(lobby);
        userRepository.save(databaseUser);
    }
    public void deleteLobby(Long lobbyId) {
        Lobby lobby = getLobby(lobbyId);
        lobbyRepository.delete(lobby);
    }
    public List<Member> getMembers() {
        return this.memberRepository.findAll();
    }
}