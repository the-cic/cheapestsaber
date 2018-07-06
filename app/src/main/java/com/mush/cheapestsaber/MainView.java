package com.mush.cheapestsaber;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mush.cheapestsaber.game.GameRender;

/**
 * Created by mush on 25/06/2018.
 */
public class MainView extends SurfaceView implements SurfaceHolder.Callback {

    private static final long SECOND_IN_NANOS = 1000000000;

    private MainContent content;
    private GameRender render;
    private long lastUpdateStartNanos;

    public MainView(Context context) {
        super(context);

        getHolder().addCallback(this);
        setFocusable(true);
        invalidate();
        postInvalidate();
        setWillNotDraw(false);

        content = MainContent.get();
        render = new GameRender();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("view", "created");

        setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        getSecondsSinceLastUpdate();

        render.resize(getWidth(), getHeight());
        content.getInput().resize(render.getInputArea());

        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("view", "changed");

        render.resize(width, height);
        content.getInput().resize(render.getInputArea());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("view", "destroyed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        content.getInput().onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    private void update(double secondsPerFrame) {
        content.getGame().processInput(content.getInput(), secondsPerFrame);
        content.getGame().update(secondsPerFrame);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        double secondsPerFrame = getSecondsSinceLastUpdate();

        update(secondsPerFrame);

        render.draw(canvas, content.getGame());
        render.drawFps(canvas, secondsPerFrame);

        invalidate();
    }

    private double getSecondsSinceLastUpdate() {
        long now = System.nanoTime();
        long nanosSinceLastUpdate = now - lastUpdateStartNanos;
        lastUpdateStartNanos = now;
        return (double)nanosSinceLastUpdate / SECOND_IN_NANOS;
    }
}
