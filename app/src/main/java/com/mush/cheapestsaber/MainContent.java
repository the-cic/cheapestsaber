package com.mush.cheapestsaber;

import com.mush.cheapestsaber.game.GameInput;
import com.mush.cheapestsaber.game.GameMain;

/**
 * Created by mush on 05/07/2018.
 */
public class MainContent {

    private GameInput input;
    private GameMain game;

    private static MainContent instance;

    public static MainContent get() {
        if (instance == null) {
            instance = new MainContent();
        }
        return instance;
    }

    private MainContent() {
        input = new GameInput();
        game = new GameMain();
    }

    public GameInput getInput() {
        return input;
    }

    public GameMain getGame() {
        return game;
    }
}
