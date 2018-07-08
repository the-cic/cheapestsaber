package com.mush.cheapestsaber.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.mush.cheapestsaber.game.sequence.SequenceItem;
import com.mush.cheapestsaber.game.sequence.Target;

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

    private Paint scorePaint;
    private Paint targetHilightPaint;
    private Paint targetStemPaint;
    private Paint leftTargetPaint;
    private Paint rightTargetPaint;
    private Paint leftObjectTargetPaint;
    private Paint rightObjectTargetPaint;
    private Paint hitObjectTargetPaint;
    private Paint targetPaint;
    private Paint targetDestinationPaint;
    private Path targetArrowPath;

    public GameRender() {
        scorePaint = makeFillPaint(0xffffffff);
        targetHilightPaint = makeFillPaint(0xffffffff);
        targetHilightPaint.setStrokeWidth(3);
        targetStemPaint = makeFillPaint(0xff888888);
        targetPaint = makeFillPaint(0xffffff00);
        targetDestinationPaint = makeFillPaint(0xffffff00);
        leftTargetPaint = makeFillPaint(0xffff0000);
        rightTargetPaint = makeFillPaint(0xff0000ff);
        leftObjectTargetPaint = makeFillPaint(0xffff8888);
        rightObjectTargetPaint = makeFillPaint(0xff8888ff);
        hitObjectTargetPaint = makeFillPaint(0xff88ff88);

        Typeface fpsTypeface = Typeface.create("sans-serif", Typeface.BOLD);
        scorePaint.setTextSize(20);
        scorePaint.setTypeface(fpsTypeface);

        makeTargetArrowPath(100);
    }

    public void draw(Canvas canvas, GameMain game) {
        Paint paint = makeFillPaint(0xffffffff);

        canvas.drawLine(0, inputArea.top, inputArea.width(), inputArea.top, paint);
        canvas.drawLine(inputArea.width() * 0.5f, inputArea.top, inputArea.width() * 0.5f, inputArea.bottom, paint);

        double windowDuration = game.getTargetWindowDuration();
        List<SequenceItem> targetWindow = game.getTargetWindow();

        canvas.save();

        canvas.translate(playArea.width() / 2, playArea.height() / 2);

        for (int i = targetWindow.size() - 1; i >= 0; i--) {
            SequenceItem item = targetWindow.get(i);
            if (item instanceof Target) {
                drawTarget(canvas, (Target) item, windowDuration);
            }
        }

        canvas.restore();

        PointF leftPoint = game.getLeftPoint();
        PointF rightPoint = game.getRightPoint();

        if (leftPoint != null) {
            paint.setColor(0x5fff0000);
            canvas.drawCircle((float) (screenWidth * (0.5 + leftPoint.x /2)), (float) (screenWidth * (0.5 + leftPoint.y / 2)), 2.5f, paint);
        }

        if (rightPoint != null) {
            paint.setColor(0x5f0000ff);
            canvas.drawCircle((float) (screenWidth * (0.5 + rightPoint.x /2)), (float) (screenWidth * (0.5 + rightPoint.y / 2)), 2.5f, paint);
        }

        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(0xffff0000);
        drawTool(canvas, game.getLeftTool(), paint);

        paint.setColor(0xff0000ff);
        drawTool(canvas, game.getRightTool(), paint);

        canvas.drawText("Combo", 10, 30, scorePaint);
        canvas.drawText("" + game.getComboLength(), 10, 50, scorePaint);
    }

    private void drawTool(Canvas canvas, Tool tool, Paint paint) {
        PointF position = tool.getPosition();
        PointF delayed = tool.getDelayedPosition();
        PointF start = tool.getStartPoint();
        Point direction = tool.getDirection();

        float pX = (float) (screenWidth * (0.5 + position.x /2));
        float pY = (float) (screenWidth * (0.5 + position.y / 2));

        float dpX = (float) (screenWidth * (0.5 + delayed.x /2));
        float dpY = (float) (screenWidth * (0.5 + delayed.y / 2));

        float sX = (float) (screenWidth * (0.5 + start.x /2));
        float sY = (float) (screenWidth * (0.5 + start.y / 2));

        canvas.drawCircle(pX, pY, 10, paint);
        canvas.drawRect(dpX - 10, dpY - 10, dpX + 10, dpY + 10, paint);
        canvas.drawLine(dpX, dpY, pX, pY, paint);
        canvas.drawRect(sX - 10, sY - 10, sX + 10, sY + 10, paint);

        canvas.drawLine(pX, pY, pX + direction.x * 100, pY + direction.y *100, paint);
    }

    private float getBoxSize() {
        return playArea.width() / 10;
    }

    private void drawTarget(Canvas canvas, Target target, double windowDuration) {
        float boxSize = getBoxSize();

        float x = target.xOffset * boxSize * 2;
        float y = target.yOffset * boxSize * 2;

        int saveCount = canvas.save();

        double percent = target.getCurrentTimeStartOffset() / windowDuration;
        float scale = (float) (1  / (1 + percent*2));

        canvas.scale(scale, scale);
        canvas.translate(0, (float) (-percent * boxSize * 10));

        canvas.translate(x, y);

        float rotation = getRotationForDirection(target.getDirection());

        if (rotation != 0) {
            canvas.rotate(-rotation * 180);
        }

        drawTargetBox(canvas, target, boxSize, percent);

        canvas.restoreToCount(saveCount);
    }

    private float getRotationForDirection(Point direction) {
        /*
                   0,-1
        -1,-1 0.75 0.50 0.25 1,-1
        -1, 0 1.00   .  0.00 1,0
        -1, 1 1.25 1.50 1.75 1, 1
                   0, 1
        */
        float rot = 0;
        if (direction != null) {
            if (direction.x == 1) {
                rot = 2 - direction.y * 0.25f;
                rot = rot % 2;
            } else if (direction.x == -1) {
                rot = 1 + direction.y * 0.25f;
            } else if (direction.y != 0) {
                rot = 1 + direction.y * 0.50f;
            }
        }
        return rot;
    }

    private void drawTargetBox(Canvas canvas, Target target, float size, double percent) {
        Paint paint = targetPaint;
        if (target.isHit()) {
            paint.setColor(hitObjectTargetPaint.getColor());
        }
        else if (target.getSide() == Target.SIDE_LEFT) {
            paint.setColor(target.isActive() ? leftObjectTargetPaint.getColor() : leftTargetPaint.getColor());
        } else if (target.getSide() == Target.SIDE_RIGHT) {
            paint.setColor(target.isActive() ? rightObjectTargetPaint.getColor() : rightTargetPaint.getColor());
        }

        float halfSize = size / 2;
        float margin = 0.8f;

        canvas.drawRect(- halfSize, - halfSize, + halfSize, + halfSize, paint);

        Point direction = target.getDirection();

        if (direction != null) {
            canvas.drawPath(targetArrowPath, targetHilightPaint);
        }

        // Anticipation hint
        percent = 1 - percent;
        final double minPercent = 0.8;
        if (percent < minPercent || percent > 1) {
            return;
        }
        float showPercent = (float) ((percent - minPercent) / ( 1 - minPercent));
        float iShowPercent = 1 - showPercent;
        int rgb = paint.getColor() & 0x00ffffff;
        int alpha = (int) (0xff * showPercent * 2);
        alpha = alpha > 0xff ? 0xff : alpha;

        paint = targetDestinationPaint;
        paint.setColor(rgb | (alpha << 24));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawRect(
                - halfSize - halfSize * iShowPercent * 2,
                - halfSize - halfSize * iShowPercent * 2,
                + halfSize + halfSize * iShowPercent * 2,
                + halfSize + halfSize * iShowPercent * 2, paint);
    }

    private void makeTargetArrowPath(float size){
        float halfSize = size / 2;
        float margin = 0.8f;

        Path path = new Path();
        path.moveTo(- halfSize * margin, - halfSize * margin);
        path.lineTo(- halfSize * margin, + halfSize * margin);
        path.lineTo(0, 0);
        path.lineTo(- halfSize * margin, - halfSize * margin);
        targetArrowPath = path;
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        // 2 x 1 box at the bottom
        inputArea = new RectF(0, height - width / 2, width, height);
        playArea = new RectF(0, 0, width, inputArea.top);

        makeTargetArrowPath(getBoxSize());
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
