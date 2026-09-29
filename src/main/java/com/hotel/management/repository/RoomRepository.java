package com.hotel.management.repository;

import com.hotel.management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailable(boolean available);
    List<Room> findByType(Room.RoomType type);
}
