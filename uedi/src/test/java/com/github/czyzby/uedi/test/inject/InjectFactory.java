package com.github.czyzby.uedi.test.inject;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.github.czyzby.uedi.stereotype.Factory;

public class InjectFactory implements Factory {
    public Built getBuilt() {
        return new Built();
    }

    public Double square(final Float parameter) {
        return (double) parameter * parameter;
    }

    // Ignored - static or not public:
    public static BigDecimal getZero() {
        return BigDecimal.ZERO;
    }

    private BigInteger getValue() {
        return new BigInteger(String.valueOf(hashCode()));
    }

    BigInteger getThisValue() {
        return getValue();
    }

    protected BigInteger getThatValue() {
        return getThisValue();
    }
}
