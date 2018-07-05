package com.mush.cheapestsaber.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Collections;
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

    private Paint targetHilightPaint;
    private Paint targetStemPaint;
    private Paint leftTargetPaint;
    private Paint rightTargetPaint;
    private Paint leftObjectTargetPaint;
    private Paint rightObjectTargetPaint;
    private Paint targetPaint;

    public GameRender() {
        targetHilightPaint = makeFillPaint(0xffffffff);
        targetHilightPaint.setStrokeWidth(3);
        targetStemPaint = makeFillPaint(0xff888888);
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

        double windowDuration = game.getTargetWindowDuration();
        List<Target> targetWindow = game.getTargetWindow();

        canvas.save();

        canvas.translate(playArea.width() / 2, playArea.height() / 2);

        for (int i = targetWindow.size() - 1; i >= 0; i--) {
            drawTarget(canvas, targetWindow.get(i), windowDuration);
        }

        canvas.restore();

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
        float boxSize = playArea.width() / 10;
        float bottom = playArea.height() * 0.9f;
        float halfSize = boxSize * 0.5f;

        float x = target.xOffset * boxSize * 2;
        float y = target.yOffset * boxSize * 2;

        int saveCount = canvas.save();

        double percent = target.getCurrentTimeStartOffset() / windowDuration;
        float scale = (float) (1  / (1 + percent*2));

        canvas.scale(scale, scale);
        canvas.translate(0, (float) (-percent * boxSize * 10));

        canvas.drawOval(x-5, y-5, x + 5, y+5, targetStemPaint);

        canvas.drawOval(x - 5, boxSize * 5 - 2, x + 5, boxSize * 5 + 2, targetStemPaint);
        canvas.drawLine(x, boxSize * 5, x, y, targetStemPaint);
        drawTargetBox(canvas, target, x, y, boxSize);

        canvas.restoreToCount(saveCount);
    }

    private void drawTargetBox(Canvas canvas, Target target, float x, float y, float size) {
        Paint paint = targetPaint;
        if (target.side < 0) {
            paint = target.isActive() ? leftObjectTargetPaint : leftTargetPaint;
        } else if (target.side > 0) {
            paint = target.isActive() ? rightObjectTargetPaint : rightTargetPaint;
        }

        float halfSize = size / 2;
        float margin = 0.8f;

        canvas.drawRect(x - halfSize, y - halfSize, x + halfSize, y + halfSize, paint);

        if (target.horizontal != 0) {
            float hx = x + halfSize * target.horizontal * margin;
            canvas.drawLine(hx, y - halfSize * margin, hx, y + halfSize * margin, targetHilightPaint);
        }
        if (target.vertical != 0) {
            float vy = y + halfSize * target.vertical * margin;
            canvas.drawLine(x - halfSize * margin, vy, x + halfSize * margin, vy, targetHilightPaint);
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
