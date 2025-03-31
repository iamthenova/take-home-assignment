package com.cyberspeed.testgame.game.engine.combination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SameSymbolsEvaluationStrategyTest {

    @Test
    void testProcess_withMatchingEntries() {
        var strategy = new SameSymbolsEvaluationStrategy();

        var boardSpec = new HashMap<String, Set<Coordinates>>();
        boardSpec.put("entry1", Set.of(new Coordinates(1, 1), new Coordinates(2, 2)));
        boardSpec.put("entry2",
            Set.of(new Coordinates(3, 3), new Coordinates(4, 4), new Coordinates(5, 5)));

        var combination = new ConfigWinCombination(2.0, "win_condition", 2,
            "same_symbols", List.of());
        var winCombination = new AbstractMap.SimpleEntry<>("win_1", combination);

        Map<String, List<WinningCombination>> result = strategy.process(boardSpec, winCombination);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("entry1"));
        assertTrue(result.containsKey("entry2"));
    }

    @Test
    void testProcess_withNoMatchingEntries() {
        var strategy = new SameSymbolsEvaluationStrategy();

        var boardSpec = new HashMap<String, Set<Coordinates>>();
        boardSpec.put("entry1", Set.of(new Coordinates(1, 1)));
        boardSpec.put("entry2", Set.of(new Coordinates(2, 2)));

        var combination = new ConfigWinCombination(2.0, "win_condition", 3,
            "same_symbols", List.of());
        var winCombination = new AbstractMap.SimpleEntry<>("win_2", combination);

        Map<String, List<WinningCombination>> result = strategy.process(boardSpec, winCombination);

        assertTrue(result.isEmpty());
    }

    @Test
    void testProcess_withRequiredCountLessThanOrEqualToZero() {
        SameSymbolsEvaluationStrategy strategy = new SameSymbolsEvaluationStrategy();

        var boardSpec = new HashMap<String, Set<Coordinates>>();
        boardSpec.put("entry1", Set.of(new Coordinates(1, 1)));

        var combination = new ConfigWinCombination(1.5, "win_condition", 0,
            "same_symbols", List.of());
        var winCombination = new AbstractMap.SimpleEntry<>("win_3", combination);

        Map<String, List<WinningCombination>> result = strategy.process(boardSpec, winCombination);

        assertTrue(result.isEmpty());
    }
}
