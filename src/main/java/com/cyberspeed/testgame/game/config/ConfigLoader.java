package com.cyberspeed.testgame.game.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private static final String CONFIG_FILE_DEFAULT = "config.json";

    private final ObjectMapper objectMapper;

    public ConfigLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GameConfig loadConfig(String configFilePath) throws IOException {
        if (configFilePath == null) {
            var inputStream = ConfigLoader.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE_DEFAULT);
            return objectMapper.readValue(inputStream, GameConfig.class);
        }

        var file = new File(configFilePath);
        return objectMapper.readValue(file, GameConfig.class);
    }
}
