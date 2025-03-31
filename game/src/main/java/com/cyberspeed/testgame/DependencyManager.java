package com.cyberspeed.testgame;

import com.cyberspeed.testgame.game.config.ArgumentParser;
import com.cyberspeed.testgame.game.config.ArgumentValidator;
import com.cyberspeed.testgame.game.config.ConfigLoader;
import com.cyberspeed.testgame.game.engine.GameEngine;
import com.cyberspeed.testgame.game.engine.RewardCalculator;
import com.cyberspeed.testgame.game.engine.board.GameBoardGenerator;
import com.cyberspeed.testgame.game.engine.board.ProbabilityCalculator;
import com.cyberspeed.testgame.game.engine.combination.CombinationEvaluationFacade;
import com.cyberspeed.testgame.game.engine.combination.SameSymbolsEvaluationStrategy;
import com.cyberspeed.testgame.game.engine.combination.SpecificCoordinatesEvaluationStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;

public class DependencyManager {

    private final ObjectMapper objectMapper;
    private final ArgumentParser argumentParser;
    private final ArgumentValidator argumentValidator;
    private final ConfigLoader configLoader;
    private final GameEngine gameEngine;

    public DependencyManager() {
        this.objectMapper = new ObjectMapper();
        this.argumentParser = new ArgumentParser();
        this.argumentValidator = new ArgumentValidator();
        this.configLoader = new ConfigLoader(this.objectMapper);

        var probabilityCalculator = new ProbabilityCalculator(new Random());
        var gameBoardGenerator = new GameBoardGenerator(probabilityCalculator);

        var combinationEvaluationStrategies = List.of(
            new SameSymbolsEvaluationStrategy(),
            new SpecificCoordinatesEvaluationStrategy()
        );
        var combinationEvaluator = new CombinationEvaluationFacade(
            combinationEvaluationStrategies);

        var rewardCalculator = new RewardCalculator();
        this.gameEngine = new GameEngine(gameBoardGenerator, combinationEvaluator,
            rewardCalculator);
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ArgumentParser getArgumentParser() {
        return argumentParser;
    }

    public ArgumentValidator getArgumentValidator() {
        return argumentValidator;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }
}
