package pl.madamusinski.roomoccupancyoptimizationservice.customer.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    String id;
    Currency bid;
}
