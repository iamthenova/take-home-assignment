package com.cyberspeed.testgame.game.config;

import java.math.BigDecimal;

public class ArgumentParser {

    private static final String CONFIG_FILE_ARGUMENT = "--config";
    private static final String USER_INPUT_ARGUMENT = "--betting-amount";

    public Arguments parse(String[] args) {
        String fileUrl = null;
        BigDecimal bettingAmount = null;

        for (int i = 0; i < args.length; i++) {
            if (CONFIG_FILE_ARGUMENT.equals(args[i])) {
                fileUrl = args[i + 1];
            }

            if (USER_INPUT_ARGUMENT.equals(args[i])) {
                bettingAmount = BigDecimal.valueOf(Double.parseDouble(args[i + 1]));
            }
        }

        return new Arguments(fileUrl, bettingAmount);
    }
}
