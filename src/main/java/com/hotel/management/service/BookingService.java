package com.hotel.management.service;

import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.model.Booking;
import com.hotel.management.model.Guest;
import com.hotel.management.model.Room;
import com.hotel.management.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final GuestService guestService;

    public BookingService(BookingRepository bookingRepository, RoomService roomService, GuestService guestService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public List<Booking> getBookingsByGuest(Long guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public Booking createBooking(Booking booking) {
        Guest guest = guestService.getGuestById(booking.getGuest().getId());
        Room room = roomService.getRoomById(booking.getRoom().getId());

        booking.setGuest(guest);
        booking.setRoom(room);

        room.setAvailable(false);
        roomService.updateRoom(room.getId(), room);

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);

        if (status == Booking.BookingStatus.CHECKED_OUT || status == Booking.BookingStatus.CANCELLED) {
            Room room = booking.getRoom();
            room.setAvailable(true);
            roomService.updateRoom(room.getId(), room);
        }

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }
}
