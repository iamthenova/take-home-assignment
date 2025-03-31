package com.cyberspeed.testgame.game.engine.combination;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SameSymbolsEvaluationStrategy implements CombinationEvaluationStrategy {

    private static final Set<String> SUPPORTED_GROUPS = Set.of("same_symbols");

    @Override
    public Set<String> getSupportedGroups() {
        return SUPPORTED_GROUPS;
    }

    @Override
    public Map<String, List<WinningCombination>> process(Map<String, Set<Coordinates>> boardSpec,
        Entry<String, ConfigWinCombination> winCombination) {
        var winningCombinations = new HashMap<String, List<WinningCombination>>();

        var combinationName = winCombination.getKey();
        var combination = winCombination.getValue();

        var requiredCount = combination.count();
        if (requiredCount <= 0) {
            return winningCombinations;
        }

        boardSpec.entrySet().stream()
            .filter(it -> it.getValue().size() >= requiredCount)
            .forEach(it -> winningCombinations.computeIfAbsent(it.getKey(), k -> new ArrayList<>())
                .add(new WinningCombination(combinationName, combination)));

        return winningCombinations;
    }
}
