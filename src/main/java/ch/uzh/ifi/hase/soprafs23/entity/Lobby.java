package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.OverlapColor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
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

  @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Member> lobbyMembers = new ArrayList<>();
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", insertable = false, updatable = false)
  private User user;

  @Column(nullable = false)
  private Integer lobbyMaxMembers;

  @Column(nullable = false)
  private String lobbyRegion;

  @Column(nullable = false)
  private Integer lobbyTimeLimit;

  @Column(nullable = true)
  private String lobbyDecidedLocation;

  @Column(nullable = true)
  private String lobbyDecidedSport;

  @Column(nullable = true)
  private LocalDateTime lobbyDecidedDate;

  @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Location> lobbyLocations = new ArrayList<>();

  @Column(nullable = false, unique = true)
  private String token;
  @ElementCollection
  @CollectionTable(name = "member_location_votes", joinColumns = @JoinColumn(name = "lobby_id"))
  private Set<Long> locationVotes = new HashSet<>();


  public Long getHostMemberId() {return hostMemberId; }

  public void setHostMemberId(Long hostMemberId) {this.hostMemberId = hostMemberId; }

  public List<Location> getLobbyLocations() {return lobbyLocations; }

  public void addLobbyLocation(Location location) {
      lobbyLocations.add(location);
  }

  public void removeLobbyLocation(Location location) {}

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

  public boolean hasAllMembersLockedSelections() {
      boolean endLobby = true;

      for(Member member : lobbyMembers) {
          if(!member.getHasLockedSelections()) {
              endLobby = false;
          }
      }
      return endLobby;
  }

  public boolean hasTimerRunOut() {
      return false;
  }

  public Event decideAllSelections() {
      String selectedSport = decideSport();
      Location selectedLocation = decideLocation();
      LocalDateTime selectedDate = decideDate();

      Event event = createEvent(selectedSport, selectedLocation, selectedDate);

      return event;
  }

  private String decideSport() {
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

  private Location decideLocation() {
      Hashtable<Location, Integer> locationCount = new Hashtable<>();
      Location selectedLocation = new Location();

      for(Member member : lobbyMembers) {
          for (Location location : member.getSelectedLocations()) {
              if (locationCount.get(location) == null) {
                  locationCount.put(location, 1);
              }
              else {
                  locationCount.put(location, locationCount.get(location) + 1);
              }
              selectedLocation = location;
          }
      }

      Set<Location> setOfLocations = locationCount.keySet();

      for(Location location : setOfLocations) {
          if(locationCount.get(location) > locationCount.get(selectedLocation)) {
              selectedLocation = location;
          }
      }

      return selectedLocation;
  }

  private LocalDateTime decideDate() {
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

      Set<LocalDateTime> setOfLocations = dateCount.keySet();

      for(LocalDateTime location : setOfLocations) {
          if(dateCount.get(location) > dateCount.get(selectedDate)) {
              selectedDate = location;
          }
      }

      return selectedDate;
  }

  public Event createEvent(String sport, Location location, LocalDateTime date) {return new Event();}

  public OverlapColor checkSportOverlap(String sport) {return OverlapColor.RED;}

  public OverlapColor checkLocationOverlap(Location location) {return OverlapColor.RED;}

  public OverlapColor checkDateOverlap(Date date) {return OverlapColor.RED;}

  public void addLobbyMember(Member member) {
      lobbyMembers.add(member);
  }

  public void removeLobbyMember(Member memberToRemove) {
      lobbyMembers.removeIf(member1 -> member1.equals(memberToRemove));
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

  public String getLobbyDecidedLocation() {return lobbyDecidedLocation;}

  public void setLobbyDecidedLocation(String lobbyDecidedLocation) {this.lobbyDecidedLocation = lobbyDecidedLocation;}

  public Integer getNumberOfVotesForLocation(Location location) {return 0;}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

    public void addLocationVotes(Long memberId) {
        if (!locationVotes.add(memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Member with memberId "+memberId+" has already voted");
        }
        locationVotes.add(memberId);
    }
    public void removeLocationVotes(Long memberId) {
        if (locationVotes.add(memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Member with memberId "+memberId+" has not yet voted");
        }
        locationVotes.remove(memberId);
    }
}
