
package com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy;

import java.math.BigDecimal;

public interface PriceCalculator {

    BigDecimal calculatePrice(BigDecimal basePrice);
}
