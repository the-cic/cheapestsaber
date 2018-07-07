package com.mush.cheapestsaber;

import android.content.Context;

import com.mush.cheapestsaber.game.GameInput;
import com.mush.cheapestsaber.game.GameMain;

/**
 * Created by mush on 05/07/2018.
 */
public class MainContent {

    private GameInput input;
    private GameMain game;
    private Context applicationContext;

    private static MainContent instance;

    public static MainContent get(Context appContext) {
        if (instance == null) {
            instance = new MainContent(appContext);
        }
        return instance;
    }

    private MainContent(Context appContext) {
        this.applicationContext = appContext;
        input = new GameInput();
        game = new GameMain(appContext);
    }

    public GameInput getInput() {
        return input;
    }

    public GameMain getGame() {
        return game;
    }
}
