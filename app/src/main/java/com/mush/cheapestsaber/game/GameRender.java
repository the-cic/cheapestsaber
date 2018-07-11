package com.mush.cheapestsaber.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.game.sequence.SequenceItem;
import com.mush.cheapestsaber.game.sequence.Target;

import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class GameRender extends StateRender {

    private RectF inputArea;
    private RectF playArea;

    private PaintPalette paints;

    private GameMain game;

    private Path targetArrowPath;
    private Path targetBoxPath;

    private final float boxSizeToScreenFactor = 0.2f;

    public GameRender(GameMain main) {
        paints = new PaintPalette();
        game = main;

        makeTargetArrowPath(100);
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(0, inputArea.top, inputArea.width(), inputArea.top, paints.gridPaint);
        canvas.drawLine(0, inputArea.bottom, inputArea.width(), inputArea.bottom, paints.gridPaint);
        canvas.drawLine(inputArea.width() * 0.5f, inputArea.top, inputArea.width() * 0.5f, inputArea.bottom, paints.gridPaint);

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

//        PointF leftPoint = game.getLeftPoint();
//        PointF rightPoint = game.getRightPoint();

//        if (leftPoint != null) {
//            paint.setColor(0x5fff0000);
//            canvas.drawCircle((float) (screenWidth * (0.5 + leftPoint.x /2)), (float) (screenWidth * (0.5 + leftPoint.y / 2)), 2.5f, paint);
//        }
//
//        if (rightPoint != null) {
//            paint.setColor(0x5f0000ff);
//            canvas.drawCircle((float) (screenWidth * (0.5 + rightPoint.x /2)), (float) (screenWidth * (0.5 + rightPoint.y / 2)), 2.5f, paint);
//        }

        paints.toolPaint.setStyle(Paint.Style.STROKE);
        paints.toolPaint.setStrokeWidth(4);

        paints.toolPaint.setColor(paints.leftActiveTargetPaint.getColor());
        drawTool(canvas, game.getLeftTool(), paints.toolPaint);

        paints.toolPaint.setColor(paints.rightActiveTargetPaint.getColor());
        drawTool(canvas, game.getRightTool(), paints.toolPaint);

        int size = 400;
        float scale = (float)screenWidth / size;
        canvas.save();
        canvas.scale(scale, scale);

        /*
        canvas.drawText("Hit", 10, 30, paints.scorePaint);
        canvas.drawText("" + game.getHitCount() + " / " + game.getTotalCount(), 10, 50, paints.scorePaint);
        canvas.drawText("Combo", 10, 80, paints.scorePaint);
        canvas.drawText("" + game.getComboLength() + " - Best: " +  game.getMaxComboLength(), 10, 100, paints.scorePaint);
        */
        paints.scorePaint.setTextSize(20);
        canvas.drawText("COMBO", 50, 40, paints.scorePaint);
        paints.scorePaint.setTextSize(30);
        canvas.drawText(String.valueOf(game.getComboLength()), 50, 70, paints.scorePaint);

        canvas.restore();
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

//        canvas.drawCircle(pX, pY, 10, paint);
//        canvas.drawRect(dpX - 10, dpY - 10, dpX + 10, dpY + 10, paint);
        canvas.drawLine(dpX, dpY, pX, pY, paint);
//        canvas.drawRect(sX - 10, sY - 10, sX + 10, sY + 10, paint);

//        canvas.drawLine(pX, pY, pX + direction.x * 100, pY + direction.y *100, paint);
    }

    private float getBoxSize() {
        return playArea.width() * boxSizeToScreenFactor;
    }

    private void drawTarget(Canvas canvas, Target target, double windowDuration) {
        float boxSize = getBoxSize();

        float x = target.xOffset * boxSize * 1;
        float y = target.yOffset * boxSize * 1;

        int saveCount = canvas.save();

        double percent = target.getCurrentTimeStartOffset() / windowDuration;
        float scale = (float) (1  / (1 + percent*2));

        canvas.scale(scale, scale);
        canvas.translate(0, (float) (-percent * boxSize * 5));

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
        Paint paint = paints.targetPaint;
        if (target.isHit()) {
            paint.setColor(paints.targetHitPaint.getColor());
        } else if (target.isMiss()) {
            paint.setColor(paints.targetMissedPaint.getColor());
        }
        else if (target.getSide() == Target.SIDE_LEFT) {
            paint.setColor(target.isActive() ? paints.leftActiveTargetPaint.getColor() : paints.leftTargetPaint.getColor());
        } else if (target.getSide() == Target.SIDE_RIGHT) {
            paint.setColor(target.isActive() ? paints.rightActiveTargetPaint.getColor() : paints.rightTargetPaint.getColor());
        }

        float halfSize = size / 2;
        float margin = 0.8f;

        canvas.drawPath(targetBoxPath, paint);

        if (!target.isHit() && !target.isActive() && !target.isMiss()) {
            paint = paints.targetDestinationPaint;
            if (target.getSide() == Target.SIDE_LEFT) {
                paint.setColor(paints.leftTargetOutlinePaint.getColor());
            } else if (target.getSide() == Target.SIDE_RIGHT) {
                paint.setColor(paints.rightTargetOutlinePaint.getColor());
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            canvas.drawPath(targetBoxPath, paint);
        }

        Point direction = target.getDirection();

        if (direction != null) {
            canvas.drawPath(targetArrowPath, paints.targetHilightPaint);
        }

        // Anticipation hint
        percent = 1 - percent;
        final double minPercent = 0.8;
        if (percent < minPercent || percent > 1) {
            return;
        }

        if (target.getSide() == Target.SIDE_LEFT) {
            paint.setColor(paints.leftActiveTargetPaint.getColor());
        } else if (target.getSide() == Target.SIDE_RIGHT) {
            paint.setColor(paints.rightActiveTargetPaint.getColor());
        }

        float showPercent = (float) ((percent - minPercent) / ( 1 - minPercent));
        float iShowPercent = 1 - showPercent;
        int rgb = paint.getColor() & 0x00ffffff;
        int alpha = (int) (0xff * showPercent * 2);
        alpha = alpha > 0xff ? 0xff : alpha;

        paint = paints.targetDestinationPaint;
        paint.setColor(rgb | (alpha << 24));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        float scale = (1 + iShowPercent * 2);
        canvas.scale(scale, scale);
        canvas.drawPath(targetBoxPath, paint);
    }

    private void makeTargetBoxPath(float size) {
        float border = size / 2;
        float margin = border * 0.7f;

        Path path = new Path();
        path.moveTo(- margin, - border);
        path.lineTo(+ margin, - border);
        path.lineTo(+ border, - margin);
        path.lineTo(+ border, + margin);
        path.lineTo(+ margin, + border);
        path.lineTo(- margin, + border);
        path.lineTo(- border, + margin);
        path.lineTo(- border, - margin);
        path.lineTo(- margin, - border);

        targetBoxPath = path;
    }

    private void makeTargetArrowPath(float size){
        float halfSize = size / 2;
        float margin = 0.8f;
        float wmargin = 0.7f;

        Path path = new Path();
        path.moveTo(- halfSize * margin, - halfSize * wmargin);
        path.lineTo(- halfSize * margin, + halfSize * wmargin);
        path.lineTo(-halfSize * (margin - wmargin), 0);
        path.lineTo(- halfSize * margin, - halfSize * wmargin);

        targetArrowPath = path;
    }

    public void resize(int width, int height) {
        super.resize(width, height);

        // 2 x 1 box at the bottom
        inputArea = new RectF(0, height * 0.9f - width / 2, width, height * 0.9f);
        playArea = new RectF(0, 0, width, height * 1.0f - width / 2);

        makeTargetArrowPath(getBoxSize());
        makeTargetBoxPath(getBoxSize());
    }

    public RectF getInputArea() {
        return inputArea;
    }

}
