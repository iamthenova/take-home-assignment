package com.cyberspeed.testgame.game.config;

import java.math.BigDecimal;

public class ArgumentValidator {

    public void validate(Arguments arguments) {
        var bettingAmount = arguments.bettingAmount();

        if (bettingAmount == null) {
            throw new IllegalArgumentException("Please provide betting amount");
        }

        if (bettingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Betting amount must be greater than zero");
        }
    }
}
