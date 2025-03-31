package com.cyberspeed.testgame.game.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record GameResult(
    String[][] matrix,
    BigDecimal reward,
    Map<String, List<String>> appliedWinCombinations,
    @JsonInclude(Include.NON_NULL)
    String appliedBonusSymbol
) {

}
