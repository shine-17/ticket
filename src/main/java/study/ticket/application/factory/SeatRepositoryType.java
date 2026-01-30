package study.ticket.application.factory;

public enum SeatRepositoryType {
    JPA("jpaSeatRepository"),
    JPA_PESSIMISTIC_LOCK("jpaPessimisticSeatRepository"),
    JPA_OPTIMISTIC_LOCK("jpaOptimisticSeatRepository");

    private final String beanName;

    SeatRepositoryType(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
