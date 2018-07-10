package com.mush.cheapestsaber;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private long lastUpdateStartNanos;

    private double lowFps;
    private double hiFps;
    private double avgFps;
    private int fpsCount;

    public MainView(Context context) {
        super(context);

        getHolder().addCallback(this);
        setFocusable(true);
        invalidate();
        postInvalidate();
        setWillNotDraw(false);

        content = MainContent.get(context.getApplicationContext());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        getSecondsSinceLastUpdate();

        content.resize(getWidth(), getHeight());

        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        content.resize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // content.freeze()?
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        content.onTouchEvent(event);

        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        double secondsPerFrame = getSecondsSinceLastUpdate();

        content.update(secondsPerFrame);

        content.draw(canvas);

        drawFps(canvas, secondsPerFrame);

        invalidate();
    }

    private double getSecondsSinceLastUpdate() {
        long now = System.nanoTime();
        long nanosSinceLastUpdate = now - lastUpdateStartNanos;
        lastUpdateStartNanos = now;
        return (double)nanosSinceLastUpdate / SECOND_IN_NANOS;
    }

    public void drawFps(Canvas canvas, double secondsPerFrame) {
        double fps = 1 / secondsPerFrame;
        if (fpsCount == 0) {
            lowFps = 100000;
            hiFps = 0;
        }
        if (fps < lowFps) {
            lowFps = fps;
        }
        if (fps > hiFps) {
            hiFps = fps;
        }
        fpsCount++;
        if (fpsCount > 30) {
            fpsCount = 0;
        }
        avgFps = avgFps * 0.9 + fps * 0.1;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff00ff00);

        canvas.drawText((int)avgFps + " " + (int)lowFps + " " + (int)hiFps, 10, 10, paint);
    }

}
