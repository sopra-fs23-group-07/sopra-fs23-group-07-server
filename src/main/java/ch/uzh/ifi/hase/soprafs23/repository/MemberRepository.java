package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Member;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("memberRepository")
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);
    Optional<Member> findByLobbyAndUser(Lobby lobby, User user);

    List<Member> findByUser(User user);
    List<Member> findByLobby(Lobby lobby);
}

