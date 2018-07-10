package com.mush.cheapestsaber;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.mush.cheapestsaber.game.GameInput;
import com.mush.cheapestsaber.game.GameMain;
import com.mush.cheapestsaber.game.GameRender;
import com.mush.cheapestsaber.score.ScoreInput;
import com.mush.cheapestsaber.score.ScoreMain;
import com.mush.cheapestsaber.score.ScoreRender;
import com.mush.cheapestsaber.select.SelectInput;
import com.mush.cheapestsaber.select.SelectMain;
import com.mush.cheapestsaber.select.SelectRender;

/**
 * Created by mush on 05/07/2018.
 */
public class MainContent implements GameMain.GameMainDelegate, ScoreMain.ScoreMainDelegate, SelectMain.SelectMainDelegate {

    public enum State {
        SELECT,
        GAME,
        SCORE
    }

    private static MainContent instance;

    private Context applicationContext;

    private State state;

    private GameInput gameInput;
    private GameMain game;
    private GameRender gameRender;

    private SelectInput selectInput;
    private SelectMain select;
    private SelectRender selectRender;

    private ScoreInput scoreInput;
    private ScoreMain score;
    private ScoreRender scoreRender;

    private int screenWidth = 100;
    private int screenHeight = 100;

    public static MainContent get(Context appContext) {
        if (instance == null) {
            instance = new MainContent(appContext);
        }
        return instance;
    }

    private MainContent(Context appContext) {
        this.applicationContext = appContext;

        setState(State.SELECT);
    }

    public void setState(State newState) {
        State oldState = this.state;

        this.state = newState;
        Log.i("main", "state : " + this.state);

        switch (this.state) {
            case SELECT:
                createSelect();
                break;
            case GAME:
                createGame();
                break;
            case SCORE:
                createScore();
                break;
        }

        Log.i("main", "clean up old state : " + oldState);
        // clean up old state
        if (oldState != null) {
            switch (oldState) {
                case SELECT:
                    select = null;
                    break;
                case GAME:
                    // game.cleanup?
                    game = null;
                    break;
                case SCORE:
                    score = null;
                    break;
            }
        }
    }

    private void createSelect() {
        select = new SelectMain();
        select.delegate = this;
        selectInput = new SelectInput();
        selectRender = new SelectRender();
        applyScreenSize();
    }

    private void createGame() {
        gameInput = new GameInput();
        game = new GameMain(applicationContext);
        game.delegate = this;
        gameRender = new GameRender();
        applyScreenSize();
        game.setPaused(false);
    }

    private void createScore() {
        score = new ScoreMain(game);
        score.delegate = this;

        scoreInput = new ScoreInput();
        scoreRender = new ScoreRender();
        applyScreenSize();
    }

    private void applyScreenSize() {
        switch (state) {
            case SELECT:
                selectRender.resize(screenWidth, screenHeight);
                break;
            case GAME:
                gameRender.resize(screenWidth, screenHeight);
                gameInput.resize(gameRender.getInputArea());
                break;
            case SCORE:
                scoreRender.resize(screenWidth, screenHeight);
                break;
        }
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        applyScreenSize();
    }

    public void update(double secondsPerFrame) {
        switch (state) {
            case SELECT:
                select.processInput(selectInput);
                select.update(secondsPerFrame);
                break;
            case GAME:
                game.processInput(gameInput);
                game.update(secondsPerFrame);
                break;
            case SCORE:
                score.processInput(scoreInput);
                score.update(secondsPerFrame);
                break;
        }
    }

    public void onTouchEvent(MotionEvent event) {
        switch (state) {
            case SELECT:
                selectInput.onTouchEvent(event);
                break;
            case GAME:
                gameInput.onTouchEvent(event);
                break;
            case SCORE:
                scoreInput.onTouchEvent(event);
                break;
        }
    }

    public void draw(Canvas canvas) {
        switch (state) {
            case SELECT:
                selectRender.draw(canvas, select);
                break;
            case GAME:
                gameRender.draw(canvas, game);
                break;
            case SCORE:
                scoreRender.draw(canvas, score);
                break;
        }
    }

    @Override
    public void onGameFinished() {
        setState(State.SCORE);
    }

    @Override
    public void onScoreFinished() {
        setState(State.SELECT);
    }

    @Override
    public void onSelectFinished() {
        setState(State.GAME);
    }

}
