package study.ticket.application.service;

import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<Booking> findById(String id);
    Optional<Booking> findByMemberId(String loginId);
    List<Booking> findByIds(List<Long> ids);
    List<Booking> findAll();
    void book(String loginId, List<Long> seatIds);
    void save(Booking booking);
    void save(List<Booking> bookings);
}
