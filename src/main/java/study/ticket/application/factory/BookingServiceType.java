package study.ticket.application.factory;

public enum BookingServiceType {
    SYNCHRONIZED("bookingServiceImplV1"),
    REENTRANTLOCK_FAIR("bookingServiceImplV2"),
    REENTRANTLOCK_NONFAIR("bookingServiceImplV3"),
    PESSIMISTIC_LOCK("bookingServiceImplV4"),
    OPTIMISTIC_LOCK("bookingServiceImplV5"),
    REDIS_LUA("bookingServiceImplV6"),
    REDIS("bookingServiceImplV7");

    private final String beanName;

    BookingServiceType(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
