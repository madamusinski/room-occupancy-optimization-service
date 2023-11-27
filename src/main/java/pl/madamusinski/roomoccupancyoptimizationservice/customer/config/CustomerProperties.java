package pl.madamusinski.roomoccupancyoptimizationservice.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties("application.customer")
@Getter
@Setter
public class CustomerProperties {

    private BigDecimal minBidAt;
}

