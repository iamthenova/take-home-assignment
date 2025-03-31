package com.cyberspeed.testgame.game.engine;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cyberspeed.testgame.game.config.GameConfig;
import com.cyberspeed.testgame.game.engine.board.GameBoard;
import com.cyberspeed.testgame.game.engine.board.GameBoardGenerator;
import com.cyberspeed.testgame.game.engine.combination.CombinationEvaluationFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameEngineTest {

    private GameEngine gameEngine;
    private GameBoardGenerator gameBoardGenerator;
    private CombinationEvaluationFacade combinationEvaluationFacade;
    private RewardCalculator rewardCalculator;

    @BeforeEach
    void setUp() {
        gameBoardGenerator = mock(GameBoardGenerator.class);
        combinationEvaluationFacade = mock(CombinationEvaluationFacade.class);
        rewardCalculator = mock(RewardCalculator.class);
        gameEngine = new GameEngine(gameBoardGenerator, combinationEvaluationFacade,
            rewardCalculator);
    }

    @Test
    void testPlayWithValidInput() {
        var gameConfig = mock(GameConfig.class);
        String[][] gameBoard = {{"A", "B"}, {"C", "D"}};
        BigDecimal bettingAmount = BigDecimal.valueOf(100);
        var playersWinningCombinations = Map.of("combo1", List.of("A", "B"));
        var reward = BigDecimal.valueOf(500);

        when(gameConfig.rows()).thenReturn(2);
        when(gameConfig.columns()).thenReturn(2);
        when(gameConfig.probabilities()).thenReturn(mock(GameConfig.Probabilities.class));
        when(gameConfig.winCombinations()).thenReturn(mock(Map.class));
        when(gameConfig.symbols()).thenReturn(mock(Map.class));

        var mockBoard = mock(GameBoard.class);
        when(mockBoard.board()).thenReturn(gameBoard);
        when(mockBoard.bonusSymbol()).thenReturn("BONUS");

        when(gameBoardGenerator.generateBoard(eq(2), eq(2), any()))
            .thenReturn(mockBoard);

        when(combinationEvaluationFacade.evaluate(any(), any()))
            .thenReturn(playersWinningCombinations);

        when(rewardCalculator.calculate(any(), any(), any(), any(), any()))
            .thenReturn(reward);

        var result = gameEngine.play(bettingAmount, gameConfig);

        assertNotNull(result);
        assertArrayEquals(gameBoard, result.matrix());
        assertEquals(reward.setScale(0, RoundingMode.HALF_UP), result.reward());
        assertEquals(playersWinningCombinations, result.appliedWinCombinations());
        assertEquals("BONUS", result.appliedBonusSymbol());
    }

    @Test
    void testPlayWithEmptyWinningCombinations() {
        var gameConfig = mock(GameConfig.class);
        String[][] gameBoard = {{"A", "B"}, {"C", "D"}};
        BigDecimal bettingAmount = BigDecimal.valueOf(50);

        when(gameConfig.rows()).thenReturn(2);
        when(gameConfig.columns()).thenReturn(2);
        when(gameConfig.probabilities()).thenReturn(mock(GameConfig.Probabilities.class));
        when(gameConfig.winCombinations()).thenReturn(mock(Map.class));
        when(gameConfig.symbols()).thenReturn(mock(Map.class));

        var mockBoard = mock(GameBoard.class);
        when(mockBoard.board()).thenReturn(gameBoard);
        when(mockBoard.bonusSymbol()).thenReturn("BONUS");

        when(gameBoardGenerator.generateBoard(eq(2), eq(2), any()))
            .thenReturn(mockBoard);

        when(combinationEvaluationFacade.evaluate(any(), any()))
            .thenReturn(Collections.emptyMap());

        when(rewardCalculator.calculate(any(), any(), any(), any(), any()))
            .thenReturn(BigDecimal.ZERO);

        var result = gameEngine.play(bettingAmount, gameConfig);

        assertNotNull(result);
        assertArrayEquals(gameBoard, result.matrix());
        assertEquals(BigDecimal.ZERO.setScale(0, RoundingMode.HALF_UP), result.reward());
        assertTrue(result.appliedWinCombinations().isEmpty());
        assertNull(result.appliedBonusSymbol());
    }
}
