package com.cyberspeed.testgame.game.engine.combination;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CombinationEvaluationFacade {

    private final List<CombinationEvaluationStrategy> combinationEvaluationStrategies;

    public CombinationEvaluationFacade(
        List<CombinationEvaluationStrategy> combinationEvaluationStrategies) {
        this.combinationEvaluationStrategies = combinationEvaluationStrategies;
    }

    public Map<String, List<String>> evaluate(String[][] board,
        Map<String, ConfigWinCombination> configCombinationsNameToDescription) {
        var boardSpec = new HashMap<String, Set<Coordinates>>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardSpec.computeIfAbsent(board[i][j], it -> new HashSet<>())
                    .add(new Coordinates(i, j));
            }
        }

        var result = new HashMap<String, List<WinningCombination>>();
        configCombinationsNameToDescription.entrySet()
            .forEach(entry -> combinationEvaluationStrategies.stream()
                .filter(it -> it.shouldProcess(entry.getValue()))
                .forEach(it -> {
                    var r = it.process(boardSpec, entry);
                    r.forEach((symbol, combinations) -> result.computeIfAbsent(symbol,
                        k -> new ArrayList<>()).addAll(combinations));
                }));

        return chooseWithHighestRewardInGroup(result);
    }

    private Map<String, List<String>> chooseWithHighestRewardInGroup(
        Map<String, List<WinningCombination>> result) {
        var winningCombinations = new HashMap<String, List<String>>();
        result.forEach((symbol, combinations) -> {
            var selectedCombinations = chooseWithHighestRewardInGroup(combinations);

            winningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>())
                .addAll(selectedCombinations);
        });
        return winningCombinations;
    }

    private List<String> chooseWithHighestRewardInGroup(List<WinningCombination> combinations) {
        var groupedByGroup = combinations.stream()
            .collect(Collectors.groupingBy(it -> it.configWinCombination().group()));

        return groupedByGroup.values().stream()
            .map(winningCombinations -> {
                var combinationWithMaxReward = winningCombinations.stream()
                    .max(Comparator.comparing(
                        comb -> comb.configWinCombination().rewardMultiplier()))
                    .orElseThrow();

                return combinationWithMaxReward.name();
            })
            .toList();
    }
}
