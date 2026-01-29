package study.ticket.infrastructure.command.booking;

import study.ticket.domain.Booking;

import java.util.List;

public interface BookingCommandRepository {
    void save(Booking booking);
    void save(List<Booking> bookings);
}
