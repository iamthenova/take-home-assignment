package com.cyberspeed.testgame;

import com.cyberspeed.testgame.game.SlotGame;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        var slotGame = new SlotGame(new DependencyManager());
        var result = slotGame.play(args);
        System.out.println(result);
    }
}
