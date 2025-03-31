package com.cyberspeed.testgame.game.engine.combination;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SpecificCoordinatesEvaluationStrategy implements CombinationEvaluationStrategy {

    private static final String COORDINATES_SPLIT_PATTERN = ":";

    private static final Set<String> SUPPORTED_GROUPS = Set.of(
        "horizontally_linear_symbols",
        "vertically_linear_symbols",
        "ltr_diagonally_linear_symbols",
        "rtl_diagonally_linear_symbols"
    );

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
        var coveredAreas = combination.coveredAreas();

        if (coveredAreas == null || coveredAreas.isEmpty()) {
            return winningCombinations;
        }

        var winningCombinationsCoordinates = coveredAreas.stream()
            .map(coordinateArray -> coordinateArray.stream()
                .map(it -> {
                    var strings = it.split(COORDINATES_SPLIT_PATTERN);
                    return new Coordinates(Integer.parseInt(strings[0]),
                        Integer.parseInt(strings[1]));
                })
                .toList())
            .toList();

        boardSpec.forEach((symbol, coordinates) -> {
            for (List<Coordinates> winCoordinates : winningCombinationsCoordinates) {
                if (coordinates.containsAll(winCoordinates)) {
                    winningCombinations
                        .computeIfAbsent(symbol, k -> new ArrayList<>())
                        .add(new WinningCombination(combinationName, combination));
                }
            }
        });

        return winningCombinations;
    }
}
