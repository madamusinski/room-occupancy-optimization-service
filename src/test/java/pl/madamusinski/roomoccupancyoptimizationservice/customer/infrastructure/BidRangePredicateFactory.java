package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import lombok.RequiredArgsConstructor;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.config.CustomerProperties;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class BidRangePredicateFactory {
    private final CustomerProperties properties;

    public Predicate<Customer> getPredicateForConditions(BigDecimal lowestBidAt, BigDecimal highestBidAt) {
        if(Objects.isNull(lowestBidAt) && Objects.isNull(highestBidAt)) {
            return buildPredicateWhenLowestBidAndHighestBidAreMissing();
        }
        if(Objects.isNull(lowestBidAt)) {
            return buildPredicateWhenOnlyHighestBidIsPresent(highestBidAt);
        }
        if(Objects.isNull(highestBidAt)) {
            return buildPredicateWhenOnlyLowestBidIsPresent(lowestBidAt);
        }
        return buildPredicateWhenBothLowestBidAndHighestBidArePresent(lowestBidAt, highestBidAt);
    }


    private Predicate<Customer> buildPredicateWhenLowestBidAndHighestBidAreMissing() {
        if(properties.getMinBidAt().compareTo(BigDecimal.ZERO) < 0) {
            properties.setMinBidAt(BigDecimal.valueOf(1.0));
        }
        return c -> c.getBid().getValue().compareTo(properties.getMinBidAt()) >= 0;
    }

    private Predicate<Customer> buildPredicateWhenOnlyLowestBidIsPresent(BigDecimal lowestBidAt) {
        return  c -> c.getBid().getValue().compareTo(lowestBidAt) >= 0;
    }

    private Predicate<Customer> buildPredicateWhenOnlyHighestBidIsPresent(BigDecimal highestBidAt) {
        return  c -> c.getBid().getValue().compareTo(properties.getMinBidAt()) >= 0 && c.getBid().getValue().compareTo(highestBidAt) <= 0;
    }

    private Predicate<Customer> buildPredicateWhenBothLowestBidAndHighestBidArePresent(BigDecimal lowestBidAt, BigDecimal highestBidAt) {
        return  c -> c.getBid().getValue().compareTo(lowestBidAt) >= 0 && c.getBid().getValue().compareTo(highestBidAt) <= 0;
    }
}
