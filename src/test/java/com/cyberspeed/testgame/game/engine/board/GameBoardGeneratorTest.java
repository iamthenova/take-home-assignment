package com.cyberspeed.testgame.game.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cyberspeed.testgame.game.config.GameConfig.Probabilities;
import com.cyberspeed.testgame.game.config.GameConfig.Probabilities.BonusSymbolProbability;
import com.cyberspeed.testgame.game.config.GameConfig.Probabilities.CellProbability;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameBoardGeneratorTest {

    private ProbabilityCalculator probabilityCalculator;
    private GameBoardGenerator gameBoardGenerator;

    @BeforeEach
    void setup() {
        probabilityCalculator = mock(ProbabilityCalculator.class);
        gameBoardGenerator = new GameBoardGenerator(probabilityCalculator);
    }

    @Test
    void testGenerateBoardDimensions() {
        int rows = 3;
        int columns = 4;
        var randomSymbolResult = new RandomSymbolResult(false, "A");
        when(probabilityCalculator.selectRandomSymbol(Mockito.any(), Mockito.any()))
            .thenReturn(randomSymbolResult);

        var probabilities = mock(Probabilities.class);
        when(probabilities.standardSymbols()).thenReturn(List.of());
        when(probabilities.bonusSymbols()).thenReturn(new BonusSymbolProbability(Map.of()));

        GameBoard gameBoard = gameBoardGenerator.generateBoard(rows, columns, probabilities);

        assertEquals(rows, gameBoard.board().length, "Number of rows should match input");
        assertEquals(columns, gameBoard.board()[0].length, "Number of columns should match input");
    }

    @Test
    void testGenerateBoardUsesProbabilityCalculator() {
        int rows = 2;
        int columns = 2;
        var randomSymbolResult = new RandomSymbolResult(false, "A");
        when(probabilityCalculator.selectRandomSymbol(Mockito.any(), Mockito.any()))
            .thenReturn(randomSymbolResult);

        Probabilities probabilities = mock(Probabilities.class);
        when(probabilities.standardSymbols()).thenReturn(List.of(
            new CellProbability(0, 0, Map.of("A", 50, "B", 50))
        ));
        when(probabilities.bonusSymbols()).thenReturn(new BonusSymbolProbability(Map.of()));

        GameBoard gameBoard = gameBoardGenerator.generateBoard(rows, columns, probabilities);

        assertEquals(rows, gameBoard.board().length, "Number of rows should match input");
        assertEquals(columns, gameBoard.board()[0].length, "Number of columns should match input");
        Mockito.verify(probabilityCalculator, Mockito.atLeast(1))
            .selectRandomSymbol(Mockito.any(), Mockito.any());
    }

    @Test
    void testBonusSymbolIsApplied() {
        int rows = 2;
        int columns = 2;
        var bonusResult = new RandomSymbolResult(true, "+10");
        when(probabilityCalculator.selectRandomSymbol(Mockito.any(), Mockito.any())).thenReturn(
            bonusResult);
        var randomSymbolResult = new RandomSymbolResult(false, "A");
        when(probabilityCalculator.selectRandomSymbol(Mockito.any())).thenReturn(
            randomSymbolResult);

        Probabilities probabilities = mock(Probabilities.class);
        when(probabilities.standardSymbols()).thenReturn(List.of());
        when(probabilities.bonusSymbols()).thenReturn(new BonusSymbolProbability(Map.of("B", 100)));

        GameBoard gameBoard = gameBoardGenerator.generateBoard(rows, columns, probabilities);

        assertEquals(rows, gameBoard.board().length, "Number of rows should match input");
        assertEquals(columns, gameBoard.board()[0].length, "Number of columns should match input");
        assertEquals("+10", gameBoard.bonusSymbol(),
            "Bonus symbol should match the expected value");
    }
}
