package com.cyberspeed.testgame.game.engine.combination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CombinationEvaluationFacadeTest {

    @Test
    void evaluate_withSingleValidCombination_returnsExpectedResult() {
        String[][] board = {
            {"X", "O", "X"},
            {"O", "X", "O"},
            {"", "", "X"}
        };

        var combinationConfig = new ConfigWinCombination(
            2.0, "condition1", 3, "group1", List.of(List.of("0,0", "0,1", "0,2"))
        );

        var config = Map.of("winning_comb_1", combinationConfig);

        var winningCombination = new WinningCombination("combination1",
            combinationConfig);

        var mockStrategy = mock(CombinationEvaluationStrategy.class);
        when(mockStrategy.shouldProcess(combinationConfig)).thenReturn(true);
        when(mockStrategy.process(any(), any())).thenReturn(
            Map.of("X", List.of(winningCombination)));

        var facade = new CombinationEvaluationFacade(List.of(mockStrategy));

        var result = facade.evaluate(board, config);

        assertEquals(1, result.size());
        assertEquals(List.of("combination1"), result.get("X"));
    }

    @Test
    void evaluate_withMultipleCombinations_returnsHighestReward() {
        String[][] board = {
            {"X", "X", "O"},
            {"O", "X", "X"},
            {"", "X", "O"}
        };

        var combinationConfig1 = new ConfigWinCombination(
            2.0, "condition1", 3, "group1", List.of(List.of("0,0", "1,1", "2,2"))
        );

        var combinationConfig2 = new ConfigWinCombination(
            5.0, "condition2", 3, "group1", List.of(List.of("0,0", "1,1", "2,2"))
        );

        var config = Map.of(
            "winning_comb_1", combinationConfig1,
            "winning_comb_2", combinationConfig2
        );

        var winningCombination1 = new WinningCombination("combination1",
            combinationConfig1);
        var winningCombination2 = new WinningCombination("combination2",
            combinationConfig2);

        var mockStrategy = mock(CombinationEvaluationStrategy.class);
        when(mockStrategy.shouldProcess(any())).thenReturn(true);
        when(mockStrategy.process(any(), any()))
            .thenReturn(Map.of("X", List.of(winningCombination1, winningCombination2)));

        var facade = new CombinationEvaluationFacade(List.of(mockStrategy));

        var result = facade.evaluate(board, config);

        assertEquals(1, result.size());
        assertEquals(List.of("combination2"), result.get("X"));
    }

    @Test
    void evaluate_withNoStrategiesMatching_returnsEmptyResult() {
        String[][] board = {
            {"X", "O", "X"},
            {"O", "X", "O"},
            {"X", "", "O"}
        };

        var config = new HashMap<String, ConfigWinCombination>();

        var mockStrategy = mock(CombinationEvaluationStrategy.class);
        when(mockStrategy.shouldProcess(any())).thenReturn(false);

        var facade = new CombinationEvaluationFacade(List.of(mockStrategy));

        var result = facade.evaluate(board, config);

        assertTrue(result.isEmpty());
    }
}
