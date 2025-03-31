package com.cyberspeed.testgame.game.engine;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record GameResult(
    String[][] matrix,
    BigDecimal reward,
    Map<String, List<String>> appliedWinCombinations,
    String appliedBonusSymbol
) {

}
