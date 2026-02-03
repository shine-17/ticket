package study.ticket.interfaces.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private long id;
    private String memberName;

    private String showName;
    private LocalDate showDate;

    private String zone;
    private int row;
    private int number;

    private String message;

    @Override
    public String toString() {
        return "BookResponse{" +
                "예매번호: " + id +
                ", 회원명: '" + memberName + '\'' +
                ", 공연명:'" + showName + '\'' +
                ", 공연날짜: " + showDate +
                ", 공연구역: '" + zone + '\'' +
                ", 공연열: " + row +
                ", 공연좌석: " + number +
                ", message: '" + message + '\'' +
                '}';
    }
}
