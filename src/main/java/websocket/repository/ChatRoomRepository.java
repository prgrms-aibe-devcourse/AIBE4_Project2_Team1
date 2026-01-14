package websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import websocket.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserAAndUserB(String userA, String userB);
}
