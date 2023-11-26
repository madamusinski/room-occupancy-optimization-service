package pl.madamusinski.roomoccupancyoptimizationservice.customer;

import lombok.experimental.UtilityClass;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Currency;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.CurrencyType;
import pl.madamusinski.roomoccupancyoptimizationservice.customer.domain.Customer;

import java.math.BigDecimal;

@UtilityClass
public class CustomerUtility {

    public static Customer createNewCustomer(BigDecimal bid, CurrencyType currencyType) {
        return createCustomer(null, bid, currencyType);
    }

    public static Customer createCustomer(String id, BigDecimal bid, CurrencyType currencyType) {
        return Customer.builder().id(id).bid(
                Currency.builder().value(bid).type(currencyType).build()
        ).build();
    }
}
