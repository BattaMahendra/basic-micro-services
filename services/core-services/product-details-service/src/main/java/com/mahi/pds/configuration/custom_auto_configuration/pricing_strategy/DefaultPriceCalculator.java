
package com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DefaultPriceCalculator implements PriceCalculator {

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        // no discount
        log.warn("Default pricing is being setup");
        return basePrice;
    }
}
