
package com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class FestivalPriceCalculator implements PriceCalculator {

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        log.warn("Festival pricing is being setup");
        BigDecimal discount = basePrice.multiply(BigDecimal.valueOf(0.20));
        return basePrice.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
