package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class InMemoryTestCustomerRepository implements CustomerRepository<Customer> {

    private final HashMap<String, Customer> customerStorage = new HashMap<>();

    @Override
    public Customer save(Customer customer) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public List<Customer> findAllByBidRange(BigDecimal lowestBidAt, BigDecimal highestBidAt) {
        return null;
    }

    @Override
    public Optional<Customer> findOneById(String id) {
        return Optional.empty();
    }
}
