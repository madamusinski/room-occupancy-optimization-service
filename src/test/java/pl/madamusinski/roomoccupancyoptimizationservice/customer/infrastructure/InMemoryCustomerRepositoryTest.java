package pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.config.CustomerProperties;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.CurrencyType;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.CustomerUtility.createCustomer;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.CustomerUtility.createNewCustomer;
import static pl.madamusinski.roomoccupancyoptimizationservice.customer.infrastructure.CustomerRepositoryFixtures.preExistingCustomerInMemoryRepositoryFixture;

class InMemoryCustomerRepositoryTest {

    private CustomerRepository<Customer> underTest;
    @Mock
    private CustomerProperties properties;
    private BidRangePredicateFactory factory;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.factory = new BidRangePredicateFactory(properties);
        this.underTest = new InMemoryTestCustomerRepository(factory, new HashMap<>());
        when(properties.getMinBidAt()).thenReturn(BigDecimal.valueOf(1.0));
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
        underTest = preExistingCustomerInMemoryRepositoryFixture(factory, newCustomer);
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
        underTest = preExistingCustomerInMemoryRepositoryFixture(factory, customerOne, customerTwo);

        // expect
        assertThat(underTest.findAll()).containsExactlyInAnyOrder(customerOne, customerTwo);
    }

    @Test
    void givenStorageWithTwoCustomersSaved_whenFindingAllCustomers_shouldReturnTheseExactTwoCustomersFromStorage() {
        // given
        final var customerOne = createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR);
        final var customerTwo = createCustomer("2", BigDecimal.valueOf(242.40), CurrencyType.EUR);
        underTest = preExistingCustomerInMemoryRepositoryFixture(factory, customerOne, customerTwo);

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
        underTest = preExistingCustomerInMemoryRepositoryFixture(factory, customerOne);

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

    static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        "given both min and max bid range",
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("2", BigDecimal.valueOf(20), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                                createCustomer("5", BigDecimal.valueOf(100.01), CurrencyType.EUR),
                                createCustomer("6", BigDecimal.valueOf(249), CurrencyType.EUR)
                        },
                        BigDecimal.valueOf(23),
                        BigDecimal.valueOf(100),
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                        }
                ),
                Arguments.of(
                        "given only max bid range should use default min bid range",
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("2", BigDecimal.valueOf(20), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                                createCustomer("5", BigDecimal.valueOf(100.01), CurrencyType.EUR),
                                createCustomer("6", BigDecimal.valueOf(249), CurrencyType.EUR)
                        },
                        null,
                        BigDecimal.valueOf(100),
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("2", BigDecimal.valueOf(20), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                        }
                ),
                Arguments.of(
                        "given only min bid range",
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("2", BigDecimal.valueOf(20), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                                createCustomer("5", BigDecimal.valueOf(100.01), CurrencyType.EUR),
                                createCustomer("6", BigDecimal.valueOf(249), CurrencyType.EUR)
                        },
                        BigDecimal.valueOf(100.01),
                        null,
                        new Customer[]{
                                createCustomer("5", BigDecimal.valueOf(100.01), CurrencyType.EUR),
                                createCustomer("6", BigDecimal.valueOf(249), CurrencyType.EUR)
                        }
                ),
                Arguments.of(
                        "given no bid range",
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("2", BigDecimal.valueOf(20), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                                createCustomer("5", BigDecimal.valueOf(100.01), CurrencyType.EUR),
                                createCustomer("6", BigDecimal.valueOf(249), CurrencyType.EUR)
                        },
                        null,
                        null,
                        new Customer[]{
                                createCustomer("1", BigDecimal.valueOf(100), CurrencyType.EUR),
                                createCustomer("2", BigDecimal.valueOf(20), CurrencyType.EUR),
                                createCustomer("3", BigDecimal.valueOf(99.99), CurrencyType.EUR),
                                createCustomer("4", BigDecimal.valueOf(23), CurrencyType.EUR),
                                createCustomer("5", BigDecimal.valueOf(100.01), CurrencyType.EUR),
                                createCustomer("6", BigDecimal.valueOf(249), CurrencyType.EUR)
                        }
                )
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testCases")
    void searchCustomersByBidRange(String caseDescription, Customer[] customers, BigDecimal minBigRange, BigDecimal maxBidRange, Customer[] expected) {
        // given
        underTest = preExistingCustomerInMemoryRepositoryFixture(factory, customers);

        // when
        final var foundCustomers = underTest.findAllByBidRange(minBigRange, maxBidRange);

        // then
        assertThat(foundCustomers).containsExactlyInAnyOrder(expected);
    }

}
