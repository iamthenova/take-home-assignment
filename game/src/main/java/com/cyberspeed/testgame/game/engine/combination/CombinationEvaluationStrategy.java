package com.cyberspeed.testgame.game.engine.combination;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface CombinationEvaluationStrategy {

    Set<String> getSupportedGroups();

    default boolean shouldProcess(ConfigWinCombination description) {
        return getSupportedGroups().contains(description.group());
    }

    Map<String, List<WinningCombination>> process(Map<String, Set<Coordinates>> boardSpec,
        Entry<String, ConfigWinCombination> winCombination);
}
