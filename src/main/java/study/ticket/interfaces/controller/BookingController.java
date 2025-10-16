package study.ticket.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.ticket.application.service.BookingService;
import study.ticket.domain.Booking;
import study.ticket.interfaces.request.BookRequest;
import study.ticket.interfaces.response.BookResponse;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/book")
    public BookResponse getBooking(BookRequest bookRequest) {
        return toBookResponse(bookingService.findById(bookRequest.getId()));
    }

    @PostMapping("/book")
    public BookResponse book(BookRequest request) {

        // 티켓 예매
        bookingService.book(request.getLoginId(), request.getSeatIds());

        // 예매 정보 반환
        Optional<Booking> booking = bookingService.findById(request.getLoginId());
//                .orElseThrow(() -> new IllegalArgumentException());

        return toBookResponse(booking);
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
