package com.cyberspeed.testgame.game.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cyberspeed.testgame.game.config.ConfigLoader;
import com.cyberspeed.testgame.game.config.GameConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigLoaderTest {

    private ConfigLoader configLoader;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = mock(ObjectMapper.class);
        this.configLoader = new ConfigLoader(this.objectMapper);
    }

    @Test
    void testLoadConfigWithValidFilePath() throws IOException {
        when(objectMapper.readValue(any(File.class), any(Class.class)))
            .thenReturn(mock(GameConfig.class));

        var configFilePath = "config.json";
        var config = configLoader.loadConfig(configFilePath);

        assertNotNull(config);
    }

    @Test
    void testLoadConfigWithNullPath() throws IOException {
        when(objectMapper.readValue(any(InputStream.class), any(Class.class)))
            .thenReturn(mock(GameConfig.class));
        var config = configLoader.loadConfig(null);

        assertNotNull(config);
    }
}
