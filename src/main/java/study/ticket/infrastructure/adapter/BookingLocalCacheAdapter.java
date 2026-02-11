package study.ticket.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import study.ticket.application.port.BookingCachePort;
import study.ticket.application.service.query.booking.BookingQuery;
import java.util.Optional;

@Component
@CacheConfig
@RequiredArgsConstructor
public class BookingLocalCacheAdapter implements BookingCachePort {

    @Cacheable(cacheNames = "bookingQuery", key = "#id")
    @Override
    public Optional<BookingQuery> find(long id) {
        return Optional.empty();
    }

    @CachePut(cacheNames = "bookingQuery", key = "#bookingQuery.id")
    @Override
    public BookingQuery save(BookingQuery bookingQuery) {
        return bookingQuery;
    }
}
