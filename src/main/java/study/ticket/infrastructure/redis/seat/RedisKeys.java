package study.ticket.infrastructure.redis.seat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum RedisKeys {
    BOOKED_SEAT("show:%s:seat:booked:%s"),
    PREEMPTED_SEAT("show:%s:seat:preempted:%s"),
    BOOKING("booked:%s");

    private final String format;

    RedisKeys(String format) {
        this.format = format;
    }

    public String generateKey(Long showId, Long seatId) {
        return format.formatted(showId, seatId);
    }

    public String generateKey(Long bookingId) {
        return format.formatted(bookingId);
    }

    public List<String> generateKeys(Long showId, List<Long> seatIds) {
        List<String> keys = new ArrayList<>();
        seatIds.forEach(seatId -> keys.add(generateKey(showId, seatId)));
        return keys;
    }

    public Map<String, String> generateKeyMap(String loginId, long showId, List<Long> seatIds) {
        return generateKeys(showId, seatIds).stream()
                .collect(Collectors.toMap(
                        key -> key,
                        value -> loginId + ":" + LocalDateTime.now()
                ));
    }
}
