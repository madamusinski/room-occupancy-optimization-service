package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Currency;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.CurrencyType;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class CustomerRepositoryTest {

    private CustomerRepository<Customer> underTest;


    @BeforeEach
    void setUp() {
        this.underTest = new InMemoryTestCustomerRepository();
    }

    @Test
    void givenNewCustomer_whenCustomerIdDoesNotExistInRepository_shouldGiveItUniqueIdAndAddItToStorage() {
        // given
        var customer = Customer.builder()
                .bid(Currency.builder()
                        .value(BigDecimal.valueOf(100))
                        .type(CurrencyType.EUR).build()
                ).build();

        // when
        assertThat(underTest.findAll()).isEmpty();
        var savedCustomer = underTest.save(customer);
        assertThat(underTest.findAll()).hasSize(1);
        assertThat(underTest.findOneById(savedCustomer.getId())).isPresent().get().isEqualTo(savedCustomer);
    }
}
