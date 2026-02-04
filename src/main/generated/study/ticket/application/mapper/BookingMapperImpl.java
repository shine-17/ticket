package study.ticket.application.mapper;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.domain.Show;
import study.ticket.infrastructure.redis.dto.RedisBookingDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T14:45:34+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public RedisBookingDto toRedisBookingDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        RedisBookingDto.RedisBookingDtoBuilder redisBookingDto = RedisBookingDto.builder();

        redisBookingDto.memberName( bookingMemberName( booking ) );
        redisBookingDto.showName( bookingShowName( booking ) );
        redisBookingDto.showDate( bookingShowDate( booking ) );
        redisBookingDto.zone( bookingSeatZone( booking ) );
        redisBookingDto.row( bookingSeatRow( booking ) );
        redisBookingDto.number( bookingSeatNumber( booking ) );
        redisBookingDto.id( booking.getId() );

        return redisBookingDto.build();
    }

    @Override
    public BookingQuery toBookingQuery(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingQuery.BookingQueryBuilder bookingQuery = BookingQuery.builder();

        bookingQuery.memberName( bookingMemberName( booking ) );
        bookingQuery.showName( bookingShowName( booking ) );
        bookingQuery.showDate( bookingShowDate( booking ) );
        bookingQuery.zone( bookingSeatZone( booking ) );
        bookingQuery.row( bookingSeatRow( booking ) );
        bookingQuery.number( bookingSeatNumber( booking ) );
        bookingQuery.id( booking.getId() );

        return bookingQuery.build();
    }

    @Override
    public RedisBookingDto toRedisBookingDto(BookingQuery bookingQuery) {
        if ( bookingQuery == null ) {
            return null;
        }

        RedisBookingDto.RedisBookingDtoBuilder redisBookingDto = RedisBookingDto.builder();

        redisBookingDto.id( bookingQuery.getId() );
        redisBookingDto.memberName( bookingQuery.getMemberName() );
        redisBookingDto.showName( bookingQuery.getShowName() );
        redisBookingDto.showDate( bookingQuery.getShowDate() );
        redisBookingDto.zone( bookingQuery.getZone() );
        redisBookingDto.row( bookingQuery.getRow() );
        redisBookingDto.number( bookingQuery.getNumber() );

        return redisBookingDto.build();
    }

    @Override
    public BookingQuery toBookingQuery(RedisBookingDto redisBookingDto) {
        if ( redisBookingDto == null ) {
            return null;
        }

        BookingQuery.BookingQueryBuilder bookingQuery = BookingQuery.builder();

        bookingQuery.id( redisBookingDto.getId() );
        bookingQuery.memberName( redisBookingDto.getMemberName() );
        bookingQuery.showName( redisBookingDto.getShowName() );
        bookingQuery.showDate( redisBookingDto.getShowDate() );
        bookingQuery.zone( redisBookingDto.getZone() );
        bookingQuery.row( redisBookingDto.getRow() );
        bookingQuery.number( redisBookingDto.getNumber() );

        return bookingQuery.build();
    }

    private String bookingMemberName(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Member member = booking.getMember();
        if ( member == null ) {
            return null;
        }
        String name = member.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String bookingShowName(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Show show = booking.getShow();
        if ( show == null ) {
            return null;
        }
        String name = show.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private LocalDate bookingShowDate(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Show show = booking.getShow();
        if ( show == null ) {
            return null;
        }
        LocalDate date = show.getDate();
        if ( date == null ) {
            return null;
        }
        return date;
    }

    private String bookingSeatZone(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Seat seat = booking.getSeat();
        if ( seat == null ) {
            return null;
        }
        String zone = seat.getZone();
        if ( zone == null ) {
            return null;
        }
        return zone;
    }

    private int bookingSeatRow(Booking booking) {
        if ( booking == null ) {
            return 0;
        }
        Seat seat = booking.getSeat();
        if ( seat == null ) {
            return 0;
        }
        int row = seat.getRow();
        return row;
    }

    private int bookingSeatNumber(Booking booking) {
        if ( booking == null ) {
            return 0;
        }
        Seat seat = booking.getSeat();
        if ( seat == null ) {
            return 0;
        }
        int number = seat.getNumber();
        return number;
    }
}
