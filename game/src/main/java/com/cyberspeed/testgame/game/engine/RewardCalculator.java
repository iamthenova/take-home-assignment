package com.cyberspeed.testgame.game.engine;

import com.cyberspeed.testgame.game.config.GameConfig;
import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RewardCalculator {

    private static final String MISS = "miss";

    public BigDecimal calculate(BigDecimal bettingAmount,
        Map<String, List<String>> playersWinningCombinations,
        String appliedBonusSymbol,
        Map<String, ConfigWinCombination> configWinCombinations,
        Map<String, GameConfig.Symbol> symbols) {
        if (playersWinningCombinations.isEmpty()) {
            return BigDecimal.ZERO;
        }

        var bonusSymbolReward = calculateBonus(appliedBonusSymbol, symbols);

        var totalReward = BigDecimal.ZERO;
        for (Map.Entry<String, List<String>> entry : playersWinningCombinations.entrySet()) {
            var combinationSymbol = entry.getKey();
            var combinationNames = entry.getValue();
            var symbol = symbols.get(combinationSymbol);

            var symbolMultiplier = BigDecimal.valueOf(symbol.rewardMultiplier());
            var combinationMultiplier = BigDecimal.ONE;

            for (String combinationName : combinationNames) {
                var configWinCombination = configWinCombinations.get(combinationName);
                combinationMultiplier = combinationMultiplier.multiply(
                    BigDecimal.valueOf(configWinCombination.rewardMultiplier()));
            }

            totalReward = totalReward.add(
                bettingAmount.multiply(symbolMultiplier).multiply(combinationMultiplier));
        }

        return totalReward.multiply(bonusSymbolReward.multiplier()).add(bonusSymbolReward.extra());
    }

    private BonusSymbolReward calculateBonus(String appliedBonusSymbol,
        Map<String, GameConfig.Symbol> symbols) {
        if (appliedBonusSymbol == null || appliedBonusSymbol.isEmpty()) {
            return new BonusSymbolReward(BigDecimal.ONE, BigDecimal.ZERO);
        }

        var symbol = symbols.get(appliedBonusSymbol);
        if (MISS.equals(symbol.impact())) {
            return new BonusSymbolReward(BigDecimal.ONE, BigDecimal.ZERO);
        }

        var multiplier = BigDecimal.ONE;
        if (symbol.rewardMultiplier() != null) {
            multiplier = BigDecimal.valueOf(symbol.rewardMultiplier());
        }

        var extra = BigDecimal.ZERO;
        if (symbol.extra() != null) {
            extra = BigDecimal.valueOf(symbol.extra());
        }

        return new BonusSymbolReward(multiplier, extra);
    }

    private record BonusSymbolReward(BigDecimal multiplier, BigDecimal extra) {

    }
}
