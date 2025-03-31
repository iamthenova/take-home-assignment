package com.cyberspeed.testgame.game.engine.board;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class ProbabilityCalculatorTest {

    @Test
    void testSelectRandomSymbolWithOnlyStandardSymbols() {
        var mockRandom = RandomGenerator.of("L64X128MixRandom");
        var calculator = new ProbabilityCalculator(mockRandom);

        var symbolWeights = Map.of("A", 1, "B", 2, "C", 3);

        var result = calculator.selectRandomSymbol(symbolWeights);

        assertNotNull(result);
        assertFalse(result.isBonusSymbol());
        assertTrue(symbolWeights.containsKey(result.symbol()));
    }

    @Test
    void testSelectRandomSymbolWithStandardAndBonusSymbols() {
        RandomGenerator mockRandom = RandomGenerator.of("L64X128MixRandom");
        ProbabilityCalculator calculator = new ProbabilityCalculator(mockRandom);

        var symbolWeights = Map.of("A", 3, "B", 5);
        var bonusSymbolWeights = Map.of("Y", 2, "Z", 4);

        var result = calculator.selectRandomSymbol(symbolWeights, bonusSymbolWeights);

        assertNotNull(result);
        assertTrue(symbolWeights.containsKey(result.symbol()) || bonusSymbolWeights.containsKey(
            result.symbol()));
    }

    @Test
    void testSelectRandomSymbolWithInvalidWeightsThrowsException() {
        RandomGenerator mockRandom = RandomGenerator.of("L64X128MixRandom");
        ProbabilityCalculator calculator = new ProbabilityCalculator(mockRandom);

        var invalidWeights = new HashMap<String, Integer>();

        assertThrows(IllegalArgumentException.class,
            () -> calculator.selectRandomSymbol(invalidWeights));
    }
}
