package com.cyberspeed.testgame.game.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class ArgumentParserTest {

    private final ArgumentParser argumentParser = new ArgumentParser();

    @Test
    void shouldParseBothArguments() {
        String[] args = {"--config", "config-file-path", "--betting-amount", "100.0"};
        Arguments result = argumentParser.parse(args);

        assertEquals("config-file-path", result.fileUrl());
        assertEquals(BigDecimal.valueOf(100.0), result.bettingAmount());
    }

    @Test
    void shouldParseConfigArgumentOnly() {
        String[] args = {"--config", "config-file-path"};
        Arguments result = argumentParser.parse(args);

        assertEquals("config-file-path", result.fileUrl());
        assertNull(result.bettingAmount());
    }

    @Test
    void shouldParseBettingAmountArgumentOnly() {
        String[] args = {"--betting-amount", "50.0"};
        Arguments result = argumentParser.parse(args);

        assertNull(result.fileUrl());
        assertEquals(BigDecimal.valueOf(50.0), result.bettingAmount());
    }

    @Test
    void shouldParseNoArguments() {
        String[] args = {};
        Arguments result = argumentParser.parse(args);

        assertNull(result.fileUrl());
        assertNull(result.bettingAmount());
    }
}
