package com.mush.cheapestsaber;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.mush.cheapestsaber.common.StateInput;
import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.game.GameInput;
import com.mush.cheapestsaber.game.GameMain;
import com.mush.cheapestsaber.game.GameRender;
import com.mush.cheapestsaber.game.Levels;
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

    private Levels levels;

    private GameMain game;
    private SelectMain select;
    private ScoreMain score;

    private StateInput input;
    private StateMain main;
    private StateRender render;

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

        levels = new Levels(appContext);

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
        input = new SelectInput();
        select = new SelectMain(levels, (SelectInput) input);
        select.delegate = this;

        render = new SelectRender(select);
        main = select;

        applyScreenSize();
    }

    private void createGame() {
        input = new GameInput();
        game = new GameMain(applicationContext, (GameInput) input, select.selectedLevel);
        game.delegate = this;

        render = new GameRender(game);
        main = game;

        applyScreenSize();
        game.setPaused(false);
    }

    private void createScore() {
        input = new ScoreInput();
        score = new ScoreMain(game, (ScoreInput) input);
        score.delegate = this;

        render = new ScoreRender(score);
        main = score;

        applyScreenSize();
    }

    private void applyScreenSize() {
        render.resize(screenWidth, screenHeight);

        switch (state) {
            case GAME:
                ((GameInput)input).resize(((GameRender)render).getInputArea());
                break;
        }
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        applyScreenSize();
    }

    public void update(double secondsPerFrame) {
        main.processInput();
        main.update(secondsPerFrame);
    }

    public void onTouchEvent(MotionEvent event) {
        input.onTouchEvent(event);
    }

    public void draw(Canvas canvas) {
        render.draw(canvas);
    }

    @Override
    public void onGameFinished() {
        game.release();
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
