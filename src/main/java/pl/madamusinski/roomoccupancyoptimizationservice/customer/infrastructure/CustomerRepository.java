package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository<E> {

    E save(E customer);

    List<E> findAll();

    List<E> findAllByBidRange(BigDecimal lowestBidAt, BigDecimal highestBidAt);

    Optional<E> findOneById(String id);
}
