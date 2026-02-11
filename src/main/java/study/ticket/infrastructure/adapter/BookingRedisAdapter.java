package study.ticket.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import study.ticket.application.mapper.BookingMapper;
import study.ticket.application.port.BookingCachePort;
import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.infrastructure.redis.dto.RedisBookingDto;
import study.ticket.infrastructure.redis.seat.RedisKeys;

import java.util.Optional;

//@Component
@RequiredArgsConstructor
public class BookingRedisAdapter implements BookingCachePort {

    private final RedisTemplate<String, RedisBookingDto> redisTemplate;
    private final BookingMapper mapper;

    @Override
    public Optional<BookingQuery> find(long id) {
        RedisBookingDto bookingDto = redisTemplate.opsForValue()
                .get(RedisKeys.BOOKING.generateKey(id));

        BookingQuery booking = mapper.toBookingQuery(bookingDto);
        return Optional.ofNullable(booking);
    }

    @Override
    public BookingQuery save(BookingQuery booking) {
        RedisBookingDto bookingDto = mapper.toRedisBookingDto(booking);

        redisTemplate.opsForValue()
                .set(RedisKeys.BOOKING.generateKey(booking.getId()), bookingDto);

        return booking;
    }
}
