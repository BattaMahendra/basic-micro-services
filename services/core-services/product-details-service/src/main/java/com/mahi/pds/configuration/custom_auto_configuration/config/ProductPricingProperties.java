
package com.mahi.pds.configuration.custom_auto_configuration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "product.pricing")
public class ProductPricingProperties {

    /**
     * Whether pricing strategy is enabled.
     */
    private boolean enabled = true;

    /**
     * Strategy to use: default, festival, b2b
     */
    private String strategy = "default";

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
