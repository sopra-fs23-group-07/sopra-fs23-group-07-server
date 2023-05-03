package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import com.fasterxml.jackson.annotation.JsonView;
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
  @Mapping(source = "bio", target = "bio")
  @Mapping(source = "events", target = "userEventGetDTOs")
  UserGetDTO convertEntityToUserGetDTO(User user);
    @Named("convertEntityToUserEventGetDTO")
    @Mapping(source = "eventLocation", target = "eventLocationDTO", qualifiedByName = "toLocationDTO")
    UserEventGetDTO convertEntityToUserEventGetDTO(Event event);

    default List<UserEventGetDTO> convertEntityListToUserEventGetDTOList(List<Event> entityList) {
        if (entityList == null) {
            return null;
        }
        return entityList.stream().map(this::convertEntityToUserEventGetDTO).collect(Collectors.toList());
    }
    @AfterMapping
    default void addEventsToUserGetDTO(User user, @MappingTarget UserGetDTO userGetDTO) {
        userGetDTO.setUserEventGetDTOs(convertEntityListToUserEventGetDTOList(user.getEvents()));
    }

    //mapping internal representation of  User to UserPutDTO
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target= "password")
  @Mapping(source = "birthdate", target = "birthdate")
  @Mapping(source = "bio", target = "bio")
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
  @Mapping(target = "lobbyDecidedLocation", ignore = true)
  @Mapping(source = "lobbyDecidedSport", target = "lobbyDecidedSport")
  @Mapping(source = "lobbyDecidedDate", target = "lobbyDecidedDate")
  @Mapping(source = "createdEventId", target = "createdEventId")
  //@Mapping(source = "lobbyLocations", target = "lobbyLocationDTOs")
  @Mapping(target = "timeRemaining", ignore = true)
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);
    @BeforeMapping
    default void setTimeRemaining(Lobby lobby, @MappingTarget LobbyGetDTO lobbyGetDTO) {
        lobbyGetDTO.setTimeRemaining(lobby.getTimeRemaining());
    }
    @BeforeMapping
    default void setLobbyDecidedLocation(Lobby lobby, @MappingTarget LobbyGetDTO lobbyGetDTO) {
        lobbyGetDTO.setLobbyDecidedLocation(convertEntityToLobbyLocationDTO(lobby.getDecidedLocation()));
    }

    @Mapping(source = "eventName", target = "eventName")
    @Mapping(source = "eventDate", target = "eventDate")
    @Mapping(source = "eventSport", target = "eventSport")
    @Mapping(source = "eventRegion", target = "eventRegion")
    @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
    @Mapping(source = "eventLocationDTO", target = "eventLocation", qualifiedByName = "toLocation")
    Event convertEventPostDTOtoEntity(EventPostDTO eventPostDTO);

    @Named("toLocation")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    Location mapLocationDTOtoLocation(LocationDTO locationDTO);

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "eventName", target = "eventName")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventParticipantsCount", target = "eventParticipantsCount")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  @Mapping(source = "eventLocation", target = "eventLocationDTO", qualifiedByName = "toLocationDTO")
  EventGetDTO convertEntityToEventGetDTO(Event event);
    @Named("toLocationDTO")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    LocationDTO mapLocationToLocationDTO(Location location);

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

  @Named("convertEntityToMessageDTO")
  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "message", target = "message")
  MessageDTO convertEntityToMessageDTO(Message message);
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

  default List<MessageDTO> convertEntityListToLobbyMessageDTOList(List<Message> entityList) {
      if (entityList == null) {
          return null;
      }
      return entityList.stream().map(this::convertEntityToMessageDTO).collect(Collectors.toList());
  }
  @AfterMapping
  default void addLobbyLocationsToLobbyGetDTO(Lobby lobby, @MappingTarget LobbyGetDTO lobbyGetDTO) {
      lobbyGetDTO.setLobbyLocationDTOs(convertEntityListToLobbyLocationDTOList(lobby.getLobbyLocations()));
  }

  @AfterMapping
  default void addLobbyMessagesToLobbyGetDTO(Lobby lobby, @MappingTarget LobbyGetDTO lobbyGetDTO) {
      lobbyGetDTO.setLobbyMessageDTOs(convertEntityListToLobbyMessageDTOList(lobby.getLobbyChat()));
  }

  @Mapping(source = "memberId", target = "memberId")
  @Mapping(source = "longitude", target = "longitude")
  @Mapping(source = "latitude", target = "latitude")
  @Mapping(source = "address", target = "address")
  Location convertLobbyLocationDTOtoEntity(LobbyLocationDTO lobbyLocationDTO);
}
