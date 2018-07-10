package com.mush.cheapestsaber;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mush.cheapestsaber.game.GameInput;
import com.mush.cheapestsaber.game.GameMain;
import com.mush.cheapestsaber.game.GameRender;

/**
 * Created by mush on 05/07/2018.
 */
public class MainContent {

    private GameInput gameInput;
    private GameMain game;
    private GameRender gameRender;
    private Context applicationContext;

    private int screenWidth = 100;
    private int screenHeight = 100;

    private static MainContent instance;

    public static MainContent get(Context appContext) {
        if (instance == null) {
            instance = new MainContent(appContext);
        }
        return instance;
    }

    private MainContent(Context appContext) {
        this.applicationContext = appContext;

        createGame();
    }

    private void createGame() {
        gameInput = new GameInput();
        game = new GameMain(applicationContext);
        gameRender = new GameRender();
        applyScreenSize();
    }

    private void applyScreenSize() {
        gameRender.resize(screenWidth, screenHeight);
        gameInput.resize(gameRender.getInputArea());
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        applyScreenSize();
    }

    public void update(double secondsPerFrame) {
        game.processInput(gameInput, secondsPerFrame);
        game.update(secondsPerFrame);
    }

    public void onTouchEvent(MotionEvent event) {
        gameInput.onTouchEvent(event);
    }

    public void draw(Canvas canvas) {
        gameRender.draw(canvas, game);
    }

}
