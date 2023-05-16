package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "MESSAGE")
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_sequence", initialValue = 1)
    private Long messageId;

    @Column
    private String username;

    @Column
    private String lobbyMessage;

    @Column(nullable = true)
    private Long lobbyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobbyId", insertable = false, updatable = false)
    private Lobby lobby;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getLobbyMessage() {
        return lobbyMessage;
    }

    public void setLobbyMessage(String message) {
        this.lobbyMessage = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }
}

