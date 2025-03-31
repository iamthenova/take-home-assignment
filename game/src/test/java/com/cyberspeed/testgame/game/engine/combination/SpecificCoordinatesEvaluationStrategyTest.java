package com.cyberspeed.testgame.game.engine.combination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SpecificCoordinatesEvaluationStrategyTest {

    private final SpecificCoordinatesEvaluationStrategy strategy = new SpecificCoordinatesEvaluationStrategy();

    @Test
    void shouldReturnEmptyMapWhenCoveredAreasIsNull() {
        var boardSpec = new HashMap<String, Set<Coordinates>>();
        var winCombination = new SimpleEntry<>("TestCombination",
            new ConfigWinCombination(2.0, "any", 3, "group", null));

        var result = strategy.process(boardSpec, winCombination);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenCoveredAreasIsEmpty() {
        var boardSpec = new HashMap<String, Set<Coordinates>>();
        var winCombination = new SimpleEntry<>("TestCombination",
            new ConfigWinCombination(2.0, "any", 3, "group", List.of()));

        var result = strategy.process(boardSpec, winCombination);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenNoMatchingSymbolsOnBoard() {
        var boardSpec = Map.of(
            "A", Set.of(new Coordinates(0, 0), new Coordinates(1, 1))
        );
        var winCombination = new SimpleEntry<>("DiagonalWin", new ConfigWinCombination(
            1.5, "any", 2, "ltr_diagonally_linear_symbols",
            List.of(List.of("0:1", "1:0"))
        ));

        var result = strategy.process(boardSpec, winCombination);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnWinningCombinationsWhenSymbolsMatchCoveredAreas() {
        var boardSpec = Map.of(
            "X", Set.of(new Coordinates(0, 0), new Coordinates(0, 1), new Coordinates(1, 0),
                new Coordinates(1, 1))
        );
        var winCombination = new SimpleEntry<>("SquareWin", new ConfigWinCombination(
            3.0, "any", 4, "group",
            List.of(List.of("0:0", "0:1", "1:0", "1:1"))
        ));

        var result = strategy.process(boardSpec, winCombination);

        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("X"));
        assertEquals(1, result.get("X").size());
        assertEquals("SquareWin", result.get("X").getFirst().name());
    }
}
