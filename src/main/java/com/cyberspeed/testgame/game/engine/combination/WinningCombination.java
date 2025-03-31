package com.cyberspeed.testgame.game.engine.combination;

import com.cyberspeed.testgame.game.config.GameConfig.ConfigWinCombination;

public record WinningCombination(
    String name,
    ConfigWinCombination configWinCombination
) {

}
