package com.cyberspeed.testgame.game.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ArgumentValidatorTest {

    private final ArgumentValidator argumentValidator = new ArgumentValidator();

    @Test
    void shouldThrowExceptionWhenBettingAmountIsNull() {
        var arguments = new Arguments("someFileUrl", null);

        assertThrows(IllegalArgumentException.class, () -> argumentValidator.validate(arguments),
            "Please provide betting amount");
    }

    @Test
    void shouldThrowExceptionWhenBettingAmountIsLessThanOrEqualToZero() {
        var arguments = new Arguments("someFileUrl", BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> argumentValidator.validate(arguments),
            "Betting amount must be greater than zero");

        var arguments2 = new Arguments("someFileUrl", BigDecimal.ONE.negate());

        assertThrows(IllegalArgumentException.class, () -> argumentValidator.validate(arguments2),
            "Betting amount must be greater than zero");
    }

    @Test
    void shouldNotThrowExceptionWhenBettingAmountIsValid() {
        var arguments = new Arguments("someFileUrl", BigDecimal.valueOf(10.0));

        assertDoesNotThrow(() -> argumentValidator.validate(arguments));
    }
}
