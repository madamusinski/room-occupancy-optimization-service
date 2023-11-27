package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.config.CustomerProperties;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.util.Arrays;
import java.util.HashMap;

@UtilityClass
public class CustomerRepositoryFixtures {

    public static InMemoryTestCustomerRepository preExistingCustomerInMemoryRepositoryFixture(CustomerProperties properties, Customer... customers) {
        var customerStorage = new HashMap<String, Customer>();
        Arrays.stream(customers).peek(c -> Assert.notNull(c.getId(), "Customer id cannot be null")).forEach(c -> customerStorage.compute(c.getId(), (k, v) -> c));
        return new InMemoryTestCustomerRepository(properties, customerStorage);
    }

}
