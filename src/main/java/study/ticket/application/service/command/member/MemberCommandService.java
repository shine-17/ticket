package study.ticket.application.service.command.member;

public interface MemberCommandService {
    void increaseBookingCount(String loginId, long showId, int seatCount, int compareCount);
}
