package com.cyberspeed.testgame.game.engine.board;

import java.util.Map;
import java.util.random.RandomGenerator;

public class ProbabilityCalculator {

    private final RandomGenerator random;

    public ProbabilityCalculator(RandomGenerator random) {
        this.random = random;
    }

    public RandomSymbolResult selectRandomSymbol(Map<String, Integer> symbolWeights,
        Map<String, Integer> bonusSymbolWeights) {
        int standardSymbolsWeight = symbolWeights.values().stream().mapToInt(Integer::intValue)
            .sum();
        int bonusSymbolsWeight = bonusSymbolWeights.values().stream().mapToInt(Integer::intValue)
            .sum();

        int randomWeight = random.nextInt(1, standardSymbolsWeight + bonusSymbolsWeight + 1);

        if (randomWeight <= standardSymbolsWeight) {
            return selectRandomSymbol(symbolWeights);
        }

        var symbol = select(bonusSymbolWeights);
        return new RandomSymbolResult(true, symbol);
    }


    public RandomSymbolResult selectRandomSymbol(Map<String, Integer> symbolWeights) {
        var symbol = select(symbolWeights);
        return new RandomSymbolResult(false, symbol);
    }

    private String select(Map<String, Integer> symbolWeights) {
        int totalWeight = symbolWeights.values().stream().mapToInt(Integer::intValue).sum();
        int randomWeight = random.nextInt(1, totalWeight + 1);
        var sortedByWeights = symbolWeights.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .toList();

        for (var entry : sortedByWeights) {
            randomWeight -= entry.getValue();
            if (randomWeight <= 0) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException("Invalid symbol weights provided.");
    }
}
