package pl.madamusinski.roomoccupancyoptimizationservice.customer.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Currency {
    private BigDecimal value;
    private CurrencyType type;
}
