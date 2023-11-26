package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.CurrencyType;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.CustomerUtility.createNewCustomer;

class CustomerRepositoryTest {

    private CustomerRepository<Customer> underTest;


    @BeforeEach
    void setUp() {
        this.underTest = new InMemoryTestCustomerRepository();
    }

    @Test
    void givenNewCustomer_whenCustomerIdDoesNotExistInRepository_shouldGiveItUniqueIdAndAddItToStorage() {
        // given
        var customer = createNewCustomer(BigDecimal.valueOf(100), CurrencyType.EUR);

        // when
        underTest.save(customer);

        // then
        assertThat(underTest.findAll()).containsExactly(customer);
    }

    @Test
    void givenNull_whenTryingToSaveCustomer_shouldNotSaveAndThrowIllegalArgumentException() {
        // expect
        assertThatThrownBy(() -> underTest.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer cannot be null");
    }

    @Test
    void givenTwoNewCustomers_whenSavedToEmptyCustomerStorage_shouldReturnTheseExactTwoCustomersFromStorage() {
        // given
        var customerOne = createNewCustomer(BigDecimal.valueOf(100), CurrencyType.EUR);
        var customerTwo = createNewCustomer(BigDecimal.valueOf(242.40), CurrencyType.EUR);

        // when
        underTest.save(customerOne);
        underTest.save(customerTwo);

        // then
        assertThat(underTest.findAll()).containsExactly(customerOne, customerTwo);
    }

    @Test
    void givenDbStorageWithTwoCustomersSaved_whenFindingAllCustomers_shouldReturnTheseExactTwoCustomersFromStorage() {
        // given
        var customerOne = createNewCustomer(BigDecimal.valueOf(100), CurrencyType.EUR);
        var customerTwo = createNewCustomer(BigDecimal.valueOf(242.40), CurrencyType.EUR);

        // when
        underTest.save(customerOne);
        underTest.save(customerTwo);
        var result = underTest.findAll();

        // then
        assertThat(underTest.findAll()).hasSize(2);
        assertThat(result).containsExactly(customerOne, customerTwo);
    }
}
