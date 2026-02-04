package study.ticket.interfaces.controller.command.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import study.ticket.application.service.command.booking.BookingCommandService;
import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.application.service.query.booking.BookingQueryService;
import study.ticket.domain.Booking;
import study.ticket.interfaces.dto.request.BookRequest;
import study.ticket.interfaces.dto.response.BookResponse;

import java.util.Optional;

@RestController
//@RequiredArgsConstructor
public class BookingCommandController {

    @Autowired
    @Qualifier("bookingServiceWithRedis")
    private BookingCommandService bookingCommandService;

    private BookingQueryService bookingQueryService;

    @PostMapping("/book")
    public BookResponse book(@RequestBody BookRequest request) {

        System.out.println(request);

        // 티켓 예매
        bookingCommandService.book(request.getLoginId(), request.getShowId(), request.getSeatIds());

        // 예매 정보 반환
        Optional<BookingQuery> booking = bookingQueryService.findById(request.getId());
//                .orElseThrow(() -> new IllegalArgumentException());

        return toBookResponse(booking);
    }

    private BookResponse toBookResponse(Optional<BookingQuery> findBooking) {
        BookingQuery booking = findBooking.orElse(null);

        if (booking == null) {
            return BookResponse.builder()
                    .message("예매가 존재하지 않습니다")
                    .build();
        }

        return BookResponse.builder()
                .id(booking.getId())
                .memberName(booking.getMemberName())
                .showDate(booking.getShowDate())
                .zone(booking.getZone())
                .row(booking.getRow())
                .number(booking.getNumber())
                .build();
    }
}
