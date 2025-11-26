package com.bobgarage.billingservice.order;

import java.math.BigDecimal;

public record ServiceSummary(String serviceTypeName, BigDecimal price) {
}
