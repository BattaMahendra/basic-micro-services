
package com.mahi.pds.configuration.custom_auto_configuration.auto_config;


import com.mahi.pds.configuration.custom_auto_configuration.config.ProductPricingProperties;
import com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy.B2BPriceCalculator;
import com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy.DefaultPriceCalculator;
import com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy.FestivalPriceCalculator;
import com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy.PriceCalculator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(ProductPricingProperties.class)
@ConditionalOnProperty(
        prefix = "product.pricing",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class ProductPricingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PriceCalculator.class)  // Create this if only there is no user defined PriceCalculator bean
    public PriceCalculator priceCalculator(ProductPricingProperties props) {
        String strategy = props.getStrategy().toLowerCase();

        return strategy.equals("festival")? new FestivalPriceCalculator()
                                            : (strategy.equals("b2b")? new B2BPriceCalculator()
                                                                        : new DefaultPriceCalculator());

    }
}
