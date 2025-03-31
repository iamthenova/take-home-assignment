package com.cyberspeed.testgame.game.engine;

import com.cyberspeed.testgame.game.config.GameConfig;
import com.cyberspeed.testgame.game.engine.board.GameBoardGenerator;
import com.cyberspeed.testgame.game.engine.combination.CombinationEvaluationFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class GameEngine {

    private final GameBoardGenerator gameBoardGenerator;
    private final CombinationEvaluationFacade combinationEvaluationFacade;
    private final RewardCalculator rewardCalculator;

    public GameEngine(GameBoardGenerator gameBoardGenerator,
        CombinationEvaluationFacade combinationEvaluationFacade,
        RewardCalculator rewardCalculator) {
        this.gameBoardGenerator = gameBoardGenerator;
        this.combinationEvaluationFacade = combinationEvaluationFacade;
        this.rewardCalculator = rewardCalculator;
    }

    public GameResult play(BigDecimal bettingAmount, GameConfig gameConfig) {
        var gameBoard = gameBoardGenerator.generateBoard(gameConfig.rows(), gameConfig.columns(),
            gameConfig.probabilities());
        var playersWinningCombinations = combinationEvaluationFacade.evaluate(gameBoard.board(),
            gameConfig.winCombinations());
        var reward = rewardCalculator.calculate(bettingAmount,
            playersWinningCombinations,
            gameBoard.bonusSymbol(),
            gameConfig.winCombinations(),
            gameConfig.symbols());

        return new GameResult(gameBoard.board(),
            reward.setScale(0, RoundingMode.HALF_UP),
            playersWinningCombinations,
            playersWinningCombinations.isEmpty() ? null : gameBoard.bonusSymbol());
    }
}
