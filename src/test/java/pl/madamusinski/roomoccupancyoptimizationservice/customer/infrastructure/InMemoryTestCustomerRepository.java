package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.config.CustomerProperties;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;


@RequiredArgsConstructor
public class InMemoryTestCustomerRepository implements CustomerRepository<Customer> {

    private final BidRangePredicateFactory factory;
    private final HashMap<String, Customer> customerStorage;

    @Override
    public Customer save(Customer customer) {
        Assert.notNull(customer, "Customer cannot be null");
        if(!customerStorage.containsKey(customer.getId())) {
            customer.setId(UUID.randomUUID().toString());
        }
        return customerStorage.compute(customer.getId(), (k, v) -> customer);
    }

    @Override
    public Optional<Customer> findOneById(String id) {
        return Optional.ofNullable(customerStorage.get(id));
    }

    @Override
    public List<Customer> findAll() {
        return customerStorage.values().stream().toList();
    }

    @Override
    public List<Customer> findAllByBidRange(BigDecimal lowestBidAt, BigDecimal highestBidAt) {
        return customerStorage.values().stream()
                .filter(factory.getPredicateForConditions(lowestBidAt, highestBidAt))
                .toList();
    }
}
