package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.CurrencyType;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.CustomerUtility.createCustomer;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.CustomerUtility.createNewCustomer;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure.CustomerRepositoryFixtures.preExistingCustomerInMemoryRepositoryFixture;

class InMemoryCustomerRepositoryTest {

    private CustomerRepository<Customer> underTest;


    @BeforeEach
    void setUp() {
        this.underTest = new InMemoryTestCustomerRepository(new HashMap<>());
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
    void givenUpdatedCustomer_whenCustomerInStorageHasSameId_shouldUpdateTheExistingCustomer() {
        // given
        final var newCustomer = createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR);
        underTest = preExistingCustomerInMemoryRepositoryFixture(newCustomer);
        final var modifiedCustomer = createCustomer("1", BigDecimal.valueOf(242.22), CurrencyType.EUR);

        // when
        final var result = underTest.save(modifiedCustomer);

        // then
        assertThat(result).isEqualTo(modifiedCustomer);
        assertThat(underTest.findAll()).containsExactly(result);
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
        final var customerOne = createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR);
        final var customerTwo = createCustomer("2", BigDecimal.valueOf(242.40), CurrencyType.EUR);
        underTest = preExistingCustomerInMemoryRepositoryFixture(customerOne, customerTwo);

        // expect
        assertThat(underTest.findAll()).containsExactlyInAnyOrder(customerOne, customerTwo);
    }

    @Test
    void givenStorageWithTwoCustomersSaved_whenFindingAllCustomers_shouldReturnTheseExactTwoCustomersFromStorage() {
        // given
        final var customerOne = createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR);
        final var customerTwo = createCustomer("2", BigDecimal.valueOf(242.40), CurrencyType.EUR);
        underTest = preExistingCustomerInMemoryRepositoryFixture(customerOne, customerTwo);

        // when
        final var foundCustomers = underTest.findAll();

        // then
        assertThat(underTest.findAll()).hasSize(2);
        assertThat(foundCustomers).containsExactlyInAnyOrder(customerOne, customerTwo);
    }

    @Test
    void shouldReturnOptionalWithCustomer_whenCustomerInStorageHasSameId() {
        // given
        final var FIXTURE_CUSTOMER_ID = "1";
        final var customerOne = createCustomer(FIXTURE_CUSTOMER_ID, BigDecimal.valueOf(100), CurrencyType.EUR);
        underTest = preExistingCustomerInMemoryRepositoryFixture(customerOne);

        // when
        final var possibleCustomer = underTest.findOneById(FIXTURE_CUSTOMER_ID);
        assertThat(possibleCustomer).isPresent().get().isEqualTo(customerOne);
    }

    @Test
    void givenCustomerId_whenDoesNotExistInStorage_shouldReturnEmptyOptional() {
        // given
        final var FIXTURE_CUSTOMER_ID = "1";

        // when
        final var possibleCustomer = underTest.findOneById(FIXTURE_CUSTOMER_ID);
        assertThat(possibleCustomer).isNotPresent();
    }
}
