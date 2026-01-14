package websocket.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import websocket.entity.BaseEntity;
import websocket.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatLog extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
}
