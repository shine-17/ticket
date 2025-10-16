package study.ticket.interfaces.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private String id;
    private String memberName;

    private String zone;
    private int row;
    private int number;

    private String message;

    @Override
    public String toString() {
        return "{" +
                "\"예매번호: \"" + "\"" + id + "\"," +
                "\"회원명: \"" + "\"" + memberName + "\"," +
                "\" 구역: \"" + "\"" + zone + "\"," +
                "\" 열: \"" + "\"" + row + "\"," +
                "\" 좌석 번호: \"" + "\"" + number + "\"}";
    }
}
