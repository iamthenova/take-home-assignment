package com.cyberspeed.testgame.game;

import com.cyberspeed.testgame.DependencyManager;
import com.cyberspeed.testgame.game.config.ArgumentParser;
import com.cyberspeed.testgame.game.config.ArgumentValidator;
import com.cyberspeed.testgame.game.config.ConfigLoader;
import com.cyberspeed.testgame.game.engine.GameEngine;
import java.io.IOException;

public class SlotGame {

    private final DependencyManager dependencyManager;
    private final ArgumentParser argumentParser;
    private final ArgumentValidator argumentValidator;
    private final ConfigLoader configLoader;
    private final GameEngine gameEngine;

    public SlotGame(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
        this.argumentParser = dependencyManager.getArgumentParser();
        this.argumentValidator = dependencyManager.getArgumentValidator();
        this.configLoader = dependencyManager.getConfigLoader();
        this.gameEngine = dependencyManager.getGameEngine();
    }

    public String play(String[] args) throws IOException {
        var arguments = argumentParser.parse(args);
        argumentValidator.validate(arguments);

        var gameConfig = configLoader.loadConfig(arguments.fileUrl());
        var gameResult = gameEngine.play(arguments.bettingAmount(), gameConfig);

        return dependencyManager.getObjectMapper().writeValueAsString(gameResult);
    }
}
