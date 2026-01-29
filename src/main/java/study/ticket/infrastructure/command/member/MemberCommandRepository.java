package study.ticket.infrastructure.command.member;

public interface MemberCommandRepository {
    int increaseBookingCount(String loginId, long showId, int seatCount, int compareCount);
}
