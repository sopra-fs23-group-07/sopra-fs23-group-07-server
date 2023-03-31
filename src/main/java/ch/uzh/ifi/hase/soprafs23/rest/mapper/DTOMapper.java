package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.entity.Event;
import ch.uzh.ifi.hase.soprafs23.rest.dto.EventGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.EventPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.EventPutDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "eventName", target = "eventName")
  @Mapping(source = "eventLocation", target = "eventLocation")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventParticipants", target = "eventParticipants")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  Event convertEventPostDTOtoEntity(EventPostDTO eventPostDTO);

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "eventName", target = "eventName")
  @Mapping(source = "eventLocation", target = "eventLocation")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventParticipants", target = "eventParticipants")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  EventGetDTO convertEntityToEventGetDTO(Event event);

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "eventName", target = "eventName")
  @Mapping(source = "eventLocation", target = "eventLocation")
  @Mapping(source = "eventDate", target = "eventDate")
  @Mapping(source = "eventSport", target = "eventSport")
  @Mapping(source = "eventRegion", target = "eventRegion")
  @Mapping(source = "eventParticipants", target = "eventParticipants")
  @Mapping(source = "eventMaxParticipants", target = "eventMaxParticipants")
  Event convertEventPutDTOtoEntity(EventPutDTO eventPutDTO);
}
