package pl.madamusinski.roomoccupancyoptimizationservice.customer.api;

import lombok.RequiredArgsConstructor;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository<Customer> customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public List<Customer> getAllByBidRange(BigDecimal lowestBidAt, BigDecimal highestBidAt) {
        return customerRepository.findAllByBidRange(lowestBidAt, highestBidAt);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> findOneById(String id) {
        return customerRepository.findOneById(id);
    }

}
