package study.ticket.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.domain.Booking;
import study.ticket.infrastructure.redis.dto.RedisBookingDto;

// componentModel : 스프링 컨테이너에 빈으로 등록
// unmappedTargetPolicy : target이 매핑되지 않았을 때 정책
@Named("MemberMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Named("BookingToRedisBookingDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberName", source = "member.name")
    @Mapping(target = "showName", source = "show.name")
    @Mapping(target = "showDate", source = "show.date")
    @Mapping(target = "zone", source = "seat.zone")
    @Mapping(target = "row", source = "seat.row")
    @Mapping(target = "number", source = "seat.number")
    // Booking -> RedisBookingDto
    RedisBookingDto toRedisBookingDto(Booking booking);

//    @Named("RedisBookingDtoToBooking")
//    @Mapping(target = "member.name", source = "memberName")
//    @Mapping(target = "show.name", source = "showName")
//    @Mapping(target = "show.date", source = "showDate")
//    @Mapping(target = "seat.zone", source = "zone")
//    @Mapping(target = "seat.row", source = "row")
//    @Mapping(target = "seat.number", source = "number")
//    // Booking -> RedisBookingDto
//    Booking toBooking(RedisBookingDto redisBookingDto);

    @Named("BookingToBookingQuery")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberName", source = "member.name")
    @Mapping(target = "showName", source = "show.name")
    @Mapping(target = "showDate", source = "show.date")
    @Mapping(target = "zone", source = "seat.zone")
    @Mapping(target = "row", source = "seat.row")
    @Mapping(target = "number", source = "seat.number")
    // Booking -> BookingQuery
    BookingQuery toBookingQuery(Booking booking);

    @Named("BookingQueryToRedisBookingDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberName", source = "memberName")
    @Mapping(target = "showName", source = "showName")
    @Mapping(target = "showDate", source = "showDate")
    @Mapping(target = "zone", source = "zone")
    @Mapping(target = "row", source = "row")
    @Mapping(target = "number", source = "number")
    // Booking -> BookingQuery
    RedisBookingDto toRedisBookingDto(BookingQuery bookingQuery);

    @Named("RedisBookingDtoToBookingQuery")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberName", source = "memberName")
    @Mapping(target = "showName", source = "showName")
    @Mapping(target = "showDate", source = "showDate")
    @Mapping(target = "zone", source = "zone")
    @Mapping(target = "row", source = "row")
    @Mapping(target = "number", source = "number")
    // Booking -> BookingQuery
    BookingQuery toBookingQuery(RedisBookingDto redisBookingDto);

}
