package com.cyberspeed.testgame.game.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cyberspeed.testgame.game.config.GameConfig;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RewardCalculatorTest {

    private RewardCalculator rewardCalculator;

    @BeforeEach
    void setUp() {
        rewardCalculator = new RewardCalculator();
    }

    @Test
    void testCalculate_EmptyWinningCombinations() {
        var result = rewardCalculator.calculate(
            BigDecimal.valueOf(100.0),
            Map.of(),
            null,
            Map.of(),
            Map.of()
        );
        assertEquals(BigDecimal.ZERO, result,
            "The reward should be zero when there are no winning combinations.");
    }

    @Test
    void testCalculate_SingleCombination() {
        var symbol = new GameConfig.Symbol(2.0, null, null, null);
        var configWinCombination = new GameConfig.ConfigWinCombination(1.5, null, null, null, null);

        var result = rewardCalculator.calculate(
            BigDecimal.valueOf(100.0),
            Map.of("symbolA", List.of("comboA")),
            null,
            Map.of("comboA", configWinCombination),
            Map.of("symbolA", symbol)
        );

        assertEquals(BigDecimal.valueOf(300), result.setScale(0, RoundingMode.HALF_UP),
            "The total reward should match the expected calculation for a single combination.");
    }

    @Test
    void testCalculate_MultipleCombinationsWithBonus() {
        var symbolA = new GameConfig.Symbol(2.0, null, null, null);
        var symbolB = new GameConfig.Symbol(1.5, null, null, null);
        var bonusSymbol = new GameConfig.Symbol(1.0, null, "bonus", 50);

        var configWinCombinationA = new GameConfig.ConfigWinCombination(1.5, null, null, null,
            null);
        var configWinCombinationB = new GameConfig.ConfigWinCombination(2.0, null, null, null,
            null);

        var result = rewardCalculator.calculate(
            BigDecimal.valueOf(100.0),
            Map.of(
                "symbolA", List.of("comboA"),
                "symbolB", List.of("comboB")
            ),
            "bonusSymbol",
            Map.of(
                "comboA", configWinCombinationA,
                "comboB", configWinCombinationB
            ),
            Map.of(
                "symbolA", symbolA,
                "symbolB", symbolB,
                "bonusSymbol", bonusSymbol
            )
        );

        assertEquals(BigDecimal.valueOf(650), result.setScale(0, RoundingMode.HALF_UP),
            "The total reward should include the bonus multiplier and extra reward.");
    }

    @Test
    void testCalculate_BonusMissImpact() {
        var symbol = new GameConfig.Symbol(2.0, null, null, null);
        var missBonus = new GameConfig.Symbol(1.0, null, "miss", null);
        var configWinCombination = new GameConfig.ConfigWinCombination(1.5, null, null, null, null);

        var result = rewardCalculator.calculate(
            BigDecimal.valueOf(100),
            Map.of("symbolA", List.of("comboA")),
            "missBonus",
            Map.of("comboA", configWinCombination),
            Map.of(
                "symbolA", symbol,
                "missBonus", missBonus
            )
        );

        assertEquals(BigDecimal.valueOf(300), result.setScale(0, RoundingMode.HALF_UP),
            "The bonus impact should not apply when it is marked as 'miss'.");
    }
}
