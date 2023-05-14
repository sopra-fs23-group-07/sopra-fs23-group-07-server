package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.entity.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class EventRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void findByEventName_success() {
        // given
        Event event = new Event();
        event.setEventName("TestEvent");
        event.setEventSport("soccer");
        event.setEventRegion("ZH");
        event.setEventMaxParticipants(10);
        event.setEventDate(LocalDateTime.parse("2023-05-05T18:00"));

        Location location = new Location();
        location.setAddress("Seestrasse 1, 8000 Zürich");
        location.setLatitude(37.782118);
        location.setLongitude(-122.420016);
        location.setEventId(event.getEventId());

        event.setEventLocation(location);

        entityManager.persist(event);
        entityManager.flush();

        // when
        Event found = eventRepository.findByEventName(event.getEventName());

        // then
        assertNotNull(found.getEventId());
        assertEquals(found.getEventName(), event.getEventName());
        assertEquals(found.getEventLocation(), event.getEventLocation());
        assertEquals(found.getEventSport(), event.getEventSport());
        assertEquals(found.getEventRegion(), event.getEventRegion());
        assertEquals(found.getEventMaxParticipants(), event.getEventMaxParticipants());
        assertEquals(found.getEventDate(), event.getEventDate());
    }
    @Test
    void findById_success() {
        Event event = new Event();
        event.setEventName("TestEvent");
        event.setEventSport("soccer");
        event.setEventRegion("ZH");
        event.setEventMaxParticipants(10);
        event.setEventDate(LocalDateTime.parse("2023-05-05T18:00"));

        Location location = new Location();
        location.setAddress("Seestrasse 1, 8000 Zürich");
        location.setLatitude(37.782118);
        location.setLongitude(-122.420016);
        location.setEventId(event.getEventId());

        event.setEventLocation(location);

        entityManager.persist(event);
        entityManager.flush();

        // when
        Optional<Event> found = eventRepository.findById(event.getEventId());

        // then
        found.ifPresent(value -> assertNotNull(value.getEventId()));
        found.ifPresent(value -> assertEquals(value.getEventLocation(), event.getEventLocation()));
        found.ifPresent(value -> assertEquals(value.getEventSport(), event.getEventSport()));
        found.ifPresent(value -> assertEquals(value.getEventRegion(), event.getEventRegion()));
        found.ifPresent(value -> assertEquals(value.getEventMaxParticipants(), event.getEventMaxParticipants()));
        found.ifPresent(value -> assertEquals(value.getEventDate(), event.getEventDate()));
    }
}
