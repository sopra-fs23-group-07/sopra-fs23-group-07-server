package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target= "password")
  @Mapping(source= "userId", target = "userId")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "token" , target = "token")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "birthdate", target = "birthdate")
  UserGetDTO convertEntityToUserGetDTO(User user);

    //mapping internal representation of  User to UserPutDTO
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target= "password")
  @Mapping(source = "birthdate", target = "birthdate")
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

  @BeforeMapping
  default void validateLobbyMaxMembers(LobbyPostDTO lobbyPostDTO) {
      if (lobbyPostDTO.getLobbyMaxMembers() < 2) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LobbyMaxMembers must be at least 2.");
      }
  }
  @Mapping(source = "lobbyName", target = "lobbyName")
  @Mapping(source = "lobbyRegion", target = "lobbyRegion")
  @Mapping(source = "lobbyMaxMembers", target= "lobbyMaxMembers")
  @Mapping(source = "lobbyTimeLimit", target= "lobbyTimeLimit")
  @Mapping(source= "hostMemberId", target = "hostMemberId")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "lobbyName", target = "lobbyName")
  @Mapping(source = "lobbyMembers", target = "memberDTOs")
  @Mapping(source = "lobbyMembersCount", target = "lobbyMembersCount")
  @Mapping(source = "lobbyMaxMembers" , target = "lobbyMaxMembers")
  @Mapping(source = "lobbyRegion", target = "lobbyRegion")
  @Mapping(source = "lobbyTimeLimit", target = "lobbyTimeLimit")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "haveAllMembersLockedSelections", target = "haveAllMembersLockedSelections")
  @Mapping(source = "lobbyDecidedLocation", target = "lobbyDecidedLocation")
  @Mapping(source = "lobbyDecidedSport", target = "lobbyDecidedSport")
  @Mapping(source = "lobbyDecidedDate", target = "lobbyDecidedDate")
  @Mapping(source = "createdEventId", target = "createdEventId")
  //@Mapping(source = "lobbyLocations", target = "lobbyLocationDTOs")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

  @Mapping(source = "eventName", target = "eventName")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  Event convertEventPostDTOtoEntity(EventPostDTO eventPostDTO);
  default Location mapStringToLocation(String string) {
      String[] coordinates = string.split(",");
      double longitude = Double.parseDouble(coordinates[0]);
      double latitude = Double.parseDouble(coordinates[1]);
      Location location = new Location();
      location.setLongitude(longitude);
      location.setLatitude(latitude);
      location.setLocation(string);
      return location;
  }
  @Mapping(source = "eventLocation", target = "eventLocation", qualifiedByName = "stringToLocation")
  void updateEventFromDto(EventPostDTO dto, @MappingTarget Event event);
   @Named("stringToLocation")
   default Location stringToLocation(String location) {
      return mapStringToLocation(location);
   }

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "eventName", target = "eventName")
  //@Mapping(source = "eventLocation", target = "eventLocation")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  EventGetDTO convertEntityToEventGetDTO(Event event);
  default String mapLocationToString(Location location) {
      double longitude = location.getLongitude();
      double latitude = location.getLatitude();
      return longitude + "," + latitude;
  }

  @Mapping(source = "eventLocation", target = "eventLocation", qualifiedByName = "locationToString")
  void updateDtoFromEvent(Event event, @MappingTarget EventGetDTO eventGetDTO);
  @Named("locationToString")
  default String locationToString(Location string) {
      return mapLocationToString(string);
  }

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "eventName", target = "eventName")
  @Mapping(source = "eventLocation", target = "eventLocation")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventParticipants", target = "eventParticipants")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  Event convertEventPutDTOtoEntity(EventPutDTO eventPutDTO);

  @Named("convertEntityToParticipantDTO")
  ParticipantDTO convertEntityToParticipantDTO(Participant participant);

  default List<ParticipantDTO> convertEntityListToParticipantDTOList(List<Participant> entityList) {
      if (entityList == null) {
          return null;
      }
      return entityList.stream().map(this::convertEntityToParticipantDTO).collect(Collectors.toList());
  }
  @AfterMapping
  default void addParticipantsToEventGetDTO(Event event, @MappingTarget EventGetDTO eventGetDTO) {
      eventGetDTO.setParticipantDTOs(convertEntityListToParticipantDTOList(event.getEventParticipants()));
  }

  @Named("convertEntityToMemberDTO")
  MemberDTO convertEntityToMemberDTO(Member member);

  default List<MemberDTO> convertEntityListToMemberDTOList(List<Member> entityList) {
      if (entityList == null) {
          return null;
      }
      return entityList.stream().map(this::convertEntityToMemberDTO).collect(Collectors.toList());
  }
  @AfterMapping
  default void addMembersToLobbyGetDTO(Lobby lobby, @MappingTarget LobbyGetDTO lobbyGetDTO) {
      lobbyGetDTO.setMemberDTOs(convertEntityListToMemberDTOList(lobby.getLobbyMembers()));
  }

  @Named("convertEntityToLobbyLocationDTO")
  @Mapping(target = "memberVotes", ignore = true)
  LobbyLocationDTO convertEntityToLobbyLocationDTO(Location location);
  @BeforeMapping
  default void setVotesFromMemberVotes(Location location, @MappingTarget LobbyLocationDTO dto) {
      // Call getMemberVotes() method here and set the votes field in the DTO
      dto.setMemberVotes(location.getMemberVotes());
  }

  default List<LobbyLocationDTO> convertEntityListToLobbyLocationDTOList(List<Location> entityList) {
      if (entityList == null) {
          return null;
      }
      return entityList.stream().map(this::convertEntityToLobbyLocationDTO).collect(Collectors.toList());
  }
  @AfterMapping
  default void addLobbyLocationsToLobbyGetDTO(Lobby lobby, @MappingTarget LobbyGetDTO lobbyGetDTO) {
      lobbyGetDTO.setLobbyLocationDTOs(convertEntityListToLobbyLocationDTOList(lobby.getLobbyLocations()));
  }
}
