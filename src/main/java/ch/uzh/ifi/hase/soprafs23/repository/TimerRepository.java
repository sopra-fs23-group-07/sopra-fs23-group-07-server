package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("timerRepository")
public interface TimerRepository extends JpaRepository<Timer, Long> {
    Timer findByTimerId(Long timerId);
}
