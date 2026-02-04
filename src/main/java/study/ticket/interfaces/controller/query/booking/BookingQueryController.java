package study.ticket.interfaces.controller.query.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.application.service.query.booking.BookingQueryService;
import study.ticket.domain.Booking;
import study.ticket.interfaces.dto.request.BookRequest;
import study.ticket.interfaces.dto.response.BookResponse;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookingQueryController {

    private final BookingQueryService bookingService;

    @GetMapping("/book")
    public BookResponse getBooking(BookRequest bookRequest) {
        return toBookResponse(bookingService.findById(bookRequest.getId()));
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
