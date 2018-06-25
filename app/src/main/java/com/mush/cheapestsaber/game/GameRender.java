package com.mush.cheapestsaber.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class GameRender {

    private double lowFps;
    private double hiFps;
    private double avgFps;
    private int fpsCount;

    private int screenWidth;
    private int screenHeight;
    private RectF inputArea;
    private RectF playArea;

    private Paint hilightPaint;
    private Paint leftTargetPaint;
    private Paint rightTargetPaint;
    private Paint leftObjectTargetPaint;
    private Paint rightObjectTargetPaint;
    private Paint targetPaint;

    public GameRender() {
        hilightPaint = makeFillPaint(0xffffffff);
        targetPaint = makeFillPaint(0xffffff00);
        leftTargetPaint = makeFillPaint(0xffff0000);
        rightTargetPaint = makeFillPaint(0xff0000ff);
        leftObjectTargetPaint = makeFillPaint(0xffff8888);
        rightObjectTargetPaint = makeFillPaint(0xff8888ff);
    }

    public void draw(Canvas canvas, GameMain game) {
        Paint paint = makeFillPaint(0xffffffff);

        float bottom = playArea.height() * 0.9f;
        canvas.drawLine(0, bottom, playArea.width(), bottom, paint);

        double windowDuration = 5.0;
        List<Target> targetWindow = game.getTargetWindow(windowDuration);

        for (Target target : targetWindow) {
            drawTarget(canvas, target, windowDuration);
        }

        PointF leftPoint = game.getLeftPoint();
        PointF rightPoint = game.getRightPoint();

        if (leftPoint != null) {
            paint.setColor(0xffff0000);
            canvas.drawCircle((float) (screenWidth * (0.5 + leftPoint.x /2)), (float) (screenWidth * (0.5 + leftPoint.y / 2)), 50, paint);
        }

        if (rightPoint != null) {
            paint.setColor(0xff0000ff);
            canvas.drawCircle((float) (screenWidth * (0.5 + rightPoint.x /2)), (float) (screenWidth * (0.5 + rightPoint.y / 2)), 50, paint);
        }
    }

    private void drawTarget(Canvas canvas, Target target, double windowDuration) {
        Paint paint = targetPaint;
        if (target.side < 0) {
            paint = target.isCurrentGoal() ? leftObjectTargetPaint : leftTargetPaint;
        } else if (target.side > 0) {
            paint = target.isCurrentGoal() ? rightObjectTargetPaint : rightTargetPaint;
        }

        float size = playArea.width() / 10;
        float bottom = playArea.height() * 0.9f;
        float halfSize = size * 0.5f;

        double scale = bottom / windowDuration;

        float x = playArea.width() * 0.5f + target.xOffset * size * 2;
        float y = (float) (bottom - target.getCurrentTimeStartOffset() * scale);

        canvas.drawRect(x - halfSize, y - size, x + halfSize, y, paint);

        if (target.horizontal != 0) {
            canvas.drawLine(x + halfSize * target.horizontal, y - size, x + halfSize * target.horizontal, y, hilightPaint);
        }
        if (target.vertical != 0) {
            canvas.drawLine(x - halfSize, y - halfSize + halfSize * target.vertical, x + halfSize, y - halfSize + halfSize * target.vertical, hilightPaint);
        }
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        // 2 x 1 box at the bottom
        inputArea = new RectF(0, height - width / 2, width, height);
        playArea = new RectF(0, 0, width, inputArea.top);
    }

    public RectF getInputArea() {
        return inputArea;
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

    private Paint makeFillPaint(int color) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }

}
