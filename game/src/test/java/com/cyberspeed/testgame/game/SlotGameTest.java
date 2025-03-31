package com.cyberspeed.testgame.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cyberspeed.testgame.DependencyManager;
import com.cyberspeed.testgame.game.config.ArgumentParser;
import com.cyberspeed.testgame.game.config.ArgumentValidator;
import com.cyberspeed.testgame.game.config.Arguments;
import com.cyberspeed.testgame.game.config.ConfigLoader;
import com.cyberspeed.testgame.game.config.GameConfig;
import com.cyberspeed.testgame.game.engine.GameEngine;
import com.cyberspeed.testgame.game.engine.GameResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlotGameTest {

    private SlotGame slotGame;
    private ArgumentParser mockArgumentParser;
    private ArgumentValidator mockArgumentValidator;
    private ConfigLoader mockConfigLoader;
    private GameEngine mockGameEngine;
    private ObjectMapper mockObjectMapper;

    @BeforeEach
    void setUp() {
        mockArgumentParser = mock(ArgumentParser.class);
        mockArgumentValidator = mock(ArgumentValidator.class);
        mockConfigLoader = mock(ConfigLoader.class);
        mockGameEngine = mock(GameEngine.class);
        mockObjectMapper = mock(ObjectMapper.class);
        var mockDependencyManager = mock(DependencyManager.class);

        when(mockDependencyManager.getArgumentParser()).thenReturn(mockArgumentParser);
        when(mockDependencyManager.getArgumentValidator()).thenReturn(mockArgumentValidator);
        when(mockDependencyManager.getConfigLoader()).thenReturn(mockConfigLoader);
        when(mockDependencyManager.getGameEngine()).thenReturn(mockGameEngine);
        when(mockDependencyManager.getObjectMapper()).thenReturn(mockObjectMapper);

        slotGame = new SlotGame(mockDependencyManager);
    }

    @Test
    void testPlaySuccess() throws IOException {
        var args = new String[]{"--fileUrl=config.json", "--bettingAmount=100"};
        var mockArguments = mock(Arguments.class);
        var mockConfig = mock(GameConfig.class);
        var mockGameResult = mock(GameResult.class);

        when(mockArgumentParser.parse(args)).thenReturn(mockArguments);
        when(mockArguments.fileUrl()).thenReturn("config.json");
        when(mockArguments.bettingAmount()).thenReturn(BigDecimal.valueOf(100.0));
        when(mockConfigLoader.loadConfig("config.json")).thenReturn(mockConfig);
        when(mockGameEngine.play(BigDecimal.valueOf(100.0), mockConfig)).thenReturn(mockGameResult);
        when(mockObjectMapper.writeValueAsString(mockGameResult)).thenReturn(
            "{\"result\":\"success\"}");

        var result = slotGame.play(args);

        assertEquals("{\"result\":\"success\"}", result);
        verify(mockArgumentParser).parse(args);
        verify(mockArgumentValidator).validate(mockArguments);
        verify(mockConfigLoader).loadConfig("config.json");
        verify(mockGameEngine).play(BigDecimal.valueOf(100.0), mockConfig);
        verify(mockObjectMapper).writeValueAsString(mockGameResult);
    }

    @Test
    void testPlayHandlesIOException() throws IOException {
        var args = new String[]{"--fileUrl=config.json", "--bettingAmount=100"};
        var mockArguments = mock(Arguments.class);

        when(mockArgumentParser.parse(args)).thenReturn(mockArguments);
        when(mockArguments.fileUrl()).thenReturn("config.json");
        when(mockConfigLoader.loadConfig("config.json")).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> slotGame.play(args));
        verify(mockArgumentValidator).validate(mockArguments);
        verify(mockConfigLoader).loadConfig("config.json");
    }

    @Test
    void testPlayHandlesValidationException() {
        var args = new String[]{"--fileUrl=config.json", "--bettingAmount=100"};
        var mockArguments = mock(Arguments.class);
        doThrow(IllegalArgumentException.class).when(mockArgumentValidator).validate(mockArguments);

        when(mockArgumentParser.parse(args)).thenReturn(mockArguments);

        assertThrows(IllegalArgumentException.class, () -> slotGame.play(args));
        verify(mockArgumentValidator).validate(mockArguments);
    }
}
