package com.cyberspeed.testgame.game.engine.board;

import com.cyberspeed.testgame.game.config.GameConfig;
import com.cyberspeed.testgame.game.engine.Coordinates;
import java.util.stream.Collectors;

public class GameBoardGenerator {

    private static final Coordinates DEFAULT_COORDINATES = new Coordinates(0, 0);

    private final ProbabilityCalculator probabilityCalculator;

    public GameBoardGenerator(ProbabilityCalculator probabilityCalculator) {
        this.probabilityCalculator = probabilityCalculator;
    }

    public GameBoard generateBoard(int rows, int columns, GameConfig.Probabilities probabilities) {
        var coordinatesToStandardSymbolProb = probabilities.standardSymbols().stream()
            .collect(Collectors.toMap(it -> new Coordinates(it.row(), it.column()),
                GameConfig.Probabilities.CellProbability::symbols));
        var bonusSymbolProb = probabilities.bonusSymbols().symbols();
        var isBonusSymbolApplied = false;

        var board = new String[rows][columns];
        String bonusSymbol = null;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                var standardSymbolsProbabilities = coordinatesToStandardSymbolProb.getOrDefault(
                    new Coordinates(row, column),
                    coordinatesToStandardSymbolProb.get(DEFAULT_COORDINATES)
                );

                RandomSymbolResult randomSymbolResult;
                if (isBonusSymbolApplied) {
                    randomSymbolResult = probabilityCalculator.selectRandomSymbol(
                        standardSymbolsProbabilities);
                } else {
                    randomSymbolResult = probabilityCalculator.selectRandomSymbol(
                        standardSymbolsProbabilities, bonusSymbolProb);
                }

                var currentSymbol = randomSymbolResult.symbol();
                board[row][column] = currentSymbol;

                if (randomSymbolResult.isBonusSymbol()) {
                    isBonusSymbolApplied = true;
                    bonusSymbol = currentSymbol;
                }
            }
        }

        return new GameBoard(board, bonusSymbol);
    }
}
