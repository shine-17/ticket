package study.ticket.interfaces.controller.query.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.ticket.application.service.query.booking.BookingQueryService;
import study.ticket.domain.Booking;
import study.ticket.interfaces.request.BookRequest;
import study.ticket.interfaces.response.BookResponse;

import java.util.Optional;

@RestController
//@RequiredArgsConstructor
public class BookingQueryController {

    @Autowired
    private BookingQueryService bookingService;

    @GetMapping("/book")
    public BookResponse getBooking(BookRequest bookRequest) {
        return toBookResponse(bookingService.findById(bookRequest.getId()));
    }

    private BookResponse toBookResponse(Optional<Booking> findBooking) {
        Booking booking = findBooking.orElse(null);

        if (booking == null) {
            return BookResponse.builder()
                    .message("예매가 존재하지 않습니다")
                    .build();
        }

        return BookResponse.builder()
                .id(booking.getId())
                .memberName(booking.getMember().getName())
                .zone(booking.getSeat().getZone())
                .row(booking.getSeat().getRow())
                .number(booking.getSeat().getNumber())
                .build();
    }
}
