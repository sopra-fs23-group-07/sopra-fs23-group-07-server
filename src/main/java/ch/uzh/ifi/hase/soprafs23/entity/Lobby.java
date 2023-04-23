package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.OverlapColor;
import ch.uzh.ifi.hase.soprafs23.repository.TimerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Internal Lobby Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lobby_seq")
  @SequenceGenerator(name = "lobby_seq", sequenceName = "lobby_sequence", initialValue = 1)
  private Long lobbyId;

  @Column(nullable = false, unique = true)
  private String lobbyName;

  @Column(nullable = false)
  private Long hostMemberId;

  @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Member> lobbyMembers = new ArrayList<>();

  @Column(nullable = true)
  private Integer lobbyMembersCount;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", insertable = false, updatable = false)
  private List<User> lobbyUsers = new ArrayList<>();

  @Column(nullable = false)
  private Integer lobbyMaxMembers;

  @Column(nullable = false)
  private String lobbyRegion;

  @Column(nullable = false)
  private Integer lobbyTimeLimit;

  @Column(nullable = true)
  private String lobbyDecidedSport;

  @Column(nullable = true)
  private LocalDateTime lobbyDecidedDate;

  @Column
  private Long createdEventId = null;

  @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Location> lobbyLocations = new ArrayList<>();

  @Column(nullable = false, unique = true)
  private String token;

  @Column
  private boolean haveAllMembersLockedSelections;

  @OneToOne(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
  private Timer timer;

  public Long getCreatedEventId() {return createdEventId;}

  public void setCreatedEventId(Long createdEventId) {this.createdEventId = createdEventId;}

  public Integer getLobbyMembersCount() {return lobbyMembers.size();}
  public Long getHostMemberId() {return hostMemberId; }

  public void setHostMemberId(Long hostMemberId) {this.hostMemberId = hostMemberId; }

  public List<Location> getLobbyLocations() {return lobbyLocations; }

  public void addLobbyLocation(Location location) {
      lobbyLocations.add(location);
  }

  public void removeLobbyLocation(Location location) {
      lobbyLocations.removeIf(location1 -> location1.equals(location));
  }

  public boolean isLobbyFull() {
      if(lobbyMembers.size() >= lobbyMaxMembers) {
          return true;
      }
      else {
          return false;
      }
  }

  public boolean isLobbyEmpty() {
      return lobbyMembers.isEmpty();
  }

  public boolean isHaveAllMembersLockedSelections() {
      boolean endLobby = true;

      for(Member member : lobbyMembers) {
          if(!member.getHasLockedSelections()) {
              endLobby = false;
          }
      }
      return endLobby;
  }
  public boolean isAtLeastTwoMembersHaveLockedSelections() {
      int members = 0;
      for (Member member : lobbyMembers) {
          if (member.getHasLockedSelections()) {
              members++;
          }
      }
      return members >= 2;
  }

  public void setHaveAllMembersLockedSelections(boolean haveAllMembersLockedSelections) {
      this.haveAllMembersLockedSelections = haveAllMembersLockedSelections;
  }

  public boolean hasTimerRunOut() {
      return getTimeRemaining() < 0;
  }

  public Event decideAllSelections() {
      String selectedSport = decideSport();
      Location selectedLocation = decideLocation();
      LocalDateTime selectedDate = decideDate();

      Event event = createEvent();

      return event;
  }

  public String decideSport() {
      Hashtable<String, Integer> sportsCount = new Hashtable<>();
      String selectedSport = "";

      for(Member member : lobbyMembers) {
          for (String sport : member.getSelectedSports()) {
              if (sportsCount.get(sport) == null) {
                  sportsCount.put(sport, 1);
              }
              else {
                  sportsCount.put(sport, sportsCount.get(sport) + 1);
              }
              selectedSport = sport;
          }
      }

      Set<String> setOfSports = sportsCount.keySet();

      for(String sport : setOfSports) {
          if(sportsCount.get(sport) > sportsCount.get(selectedSport)) {
              selectedSport = sport;
          }
      }

      return selectedSport;
  }

  public Location decideLocation() {
      Hashtable<Location, Integer> locationCount = new Hashtable<>();
      if(lobbyLocations.isEmpty()) { return null; }
      Location selectedLocation = lobbyLocations.get(0);
      selectedLocation.setLocationType("DECIDED");

      for(Location location : lobbyLocations) {
          if(location.getMemberVotes() > selectedLocation.getMemberVotes()) {
              selectedLocation.setLocationType("OTHER");
              selectedLocation = location;
              selectedLocation.setLocationType("DECIDED");
          }
      }

      return selectedLocation;
  }

  public LocalDateTime decideDate() {
      Hashtable<LocalDateTime, Integer> dateCount = new Hashtable<>();
      LocalDateTime selectedDate = LocalDateTime.now();

      for(Member member : lobbyMembers) {
          for (LocalDateTime date : member.getSelectedDates()) {
              if (dateCount.get(date) == null) {
                  dateCount.put(date, 1);
              }
              else {
                  dateCount.put(date, dateCount.get(date) + 1);
              }
              selectedDate = date;
          }
      }

      Set<LocalDateTime> setOfDates = dateCount.keySet();

      for(LocalDateTime date : setOfDates) {
          if(dateCount.get(date) > dateCount.get(selectedDate)) {
              selectedDate = date;
          }
      }

      return selectedDate;
  }

  public Event createEvent() {
      Event event = new Event();

      //lobbyDecidedLocation.setEventId(event.getEventId());
      event.setEventLocation( getDecidedLocation() );
      //event.getEventLocation().setEventId(event.getEventId());
      event.setEventName( lobbyName );
      event.setEventSport( lobbyDecidedSport );
      event.setEventRegion( lobbyRegion );
      if ( lobbyDecidedDate != null ) {
          event.setEventDate( lobbyDecidedDate );
      }
      event.setEventMaxParticipants( lobbyMaxMembers );

      //List<Participant> participants =getParticipantList(event);

      //event.setEventParticipants(participants);

      return event;
  }

  private List<Participant> getParticipantList(Event event) {
      List<Participant> participantList = new ArrayList<>();

      for(Member member : lobbyMembers) {
          Participant participant = new Participant();

          participant.setUserId(member.getUserId());
          participant.setEventId(event.getEventId());
          //participant.setEmail(member.getEmail());
          //participant.setUsername(member.getUsername());
          //participant.setStatus(member.getStatus());
          //participant.setCreationDate(member.getCreationDate());
          //participant.setBirthdate(member.getBirthdate());

          //participantList.add(participant);
          event.addEventParticipant(participant);

      }

      return participantList;
  }

  public OverlapColor checkSportOverlap(String sport) {return OverlapColor.RED;}

  public OverlapColor checkLocationOverlap(Location location) {return OverlapColor.RED;}

  public OverlapColor checkDateOverlap(Date date) {return OverlapColor.RED;}

  public void addLobbyMember(Member member) {
      lobbyMembers.add(member);
  }
  public void removeLobbyMember(Member memberToRemove) {
      lobbyMembers.removeIf(member1 -> member1.equals(memberToRemove));
  }
  public void addLobbyUser(User user) {
      lobbyUsers.add(user);
  }
  public void removeLobbyUser(User userToRemove) {
      lobbyUsers.removeIf(user1 -> user1.equals(userToRemove));
  }

  public Long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(Long lobbyId) {
    this.lobbyId = lobbyId;
  }

  public List<Member> getLobbyMembers() {return this.lobbyMembers; }

  public void setLobbyMaxMembers(Integer lobbyMaxMembers) {this.lobbyMaxMembers = lobbyMaxMembers; }

  public Integer getLobbyMaxMembers() {return this.lobbyMaxMembers;}

  public String getLobbyName() {
    return lobbyName;
  }

  public void setLobbyName(String lobbyName) {
    this.lobbyName = lobbyName;
  }

  public String getLobbyRegion() {
    return lobbyRegion;
  }

  public void setLobbyRegion(String lobbyRegion) {
    this.lobbyRegion = lobbyRegion;
  }

  public Integer getLobbyTimeLimit() {return this.lobbyTimeLimit; }

  public void setLobbyTimeLimit(Integer lobbyTimeLimit) {this.lobbyTimeLimit = lobbyTimeLimit; }

  public String getLobbyDecidedSport() {return lobbyDecidedSport; }

  public void setLobbyDecidedSport(String lobbyDecidedSport) {this.lobbyDecidedSport = lobbyDecidedSport;}

  public LocalDateTime getLobbyDecidedDate() {return lobbyDecidedDate;}

  public void setLobbyDecidedDate(LocalDateTime lobbyDecidedDate) {this.lobbyDecidedDate = lobbyDecidedDate;}

  public Integer getNumberOfVotesForLocation(Location location) {return 0;}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

    // Method to get the time remaining
    public long getTimeRemaining() {
        if (this.timer == null) {
            throw new RuntimeException("Timer is not started");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime timerEndTime = this.timer.getStartTime().plusMinutes(lobbyTimeLimit);

        return Duration.between(currentTime, timerEndTime).toMillis();
    }
    public void setTimer(Timer timer) {
      this.timer = timer;
    }
    public Timer getTimer() {
      return timer;
    }

    public Location getDecidedLocation() {
        for (Location location : lobbyLocations) {
            if ("DECIDED".equals(location.getLocationType())) {
                return location;
            }
        }
        return null;
    }
}
