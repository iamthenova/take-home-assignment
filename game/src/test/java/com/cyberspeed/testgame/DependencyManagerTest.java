package com.cyberspeed.testgame;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DependencyManagerTest {

    @Test
    void testGetGameEngine() {
        var dependencyManager = new DependencyManager();
        assertNotNull(dependencyManager.getGameEngine(), "GameEngine should not be null");
    }
}
