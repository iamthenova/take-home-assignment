package com.cyberspeed.testgame.game.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record GameConfig(
    Integer columns,
    Integer rows,
    Map<String, Symbol> symbols,
    Probabilities probabilities,
    @JsonProperty("win_combinations")
    Map<String, ConfigWinCombination> winCombinations
) {

    public record Symbol(
        @JsonProperty("reward_multiplier")
        Double rewardMultiplier,
        String type,
        String impact,
        Integer extra
    ) {

    }

    public record Probabilities(
        @JsonProperty("standard_symbols")
        List<CellProbability> standardSymbols,
        @JsonProperty("bonus_symbols")
        BonusSymbolProbability bonusSymbols
    ) {

        public record CellProbability(
            Integer column,
            Integer row,
            Map<String, Integer> symbols
        ) {

        }

        public record BonusSymbolProbability(
            Map<String, Integer> symbols
        ) {

        }
    }

    public record ConfigWinCombination(
        @JsonProperty("reward_multiplier")
        Double rewardMultiplier,
        String when,
        Integer count,
        String group,
        @JsonProperty("covered_areas")
        List<List<String>> coveredAreas
    ) {

    }
}
