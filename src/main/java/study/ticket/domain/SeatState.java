package study.ticket.domain;

import lombok.Getter;

@Getter
public enum SeatState {
    AVAILABLE('A'), HOLD('H'), ON_HOLD('O'), PREEMPT('P'), SOLD('S'), BLOCKED('B'), REFUNDED('R');

    private final char state;

    SeatState(char a) {
        state = a;
    }
}
