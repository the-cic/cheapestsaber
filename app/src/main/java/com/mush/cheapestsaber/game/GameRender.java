package com.mush.cheapestsaber.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.mush.cheapestsaber.common.ColorPalette;
import com.mush.cheapestsaber.common.PaintPalette;
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

        for (ToolHit toolHit : game.getToolHits()) {
            drawToolHit(canvas, toolHit);
        }

        paints.toolPaint.setStyle(Paint.Style.STROKE);
        paints.toolPaint.setStrokeWidth(4);

        paints.toolPaint.setColor(paints.leftActiveTargetPaint.getColor());
        drawTool(canvas, game.getLeftTool(), paints.toolPaint);

        paints.toolPaint.setColor(paints.rightActiveTargetPaint.getColor());
        drawTool(canvas, game.getRightTool(), paints.toolPaint);

        canvas.restore();

//        PointF leftPoint = game.getLeftPoint();
//        PointF rightPoint = game.getRightPoint();

//        if (leftPoint != null) {
//            panelPaint.setColor(0x5fff0000);
//            canvas.drawCircle((float) (screenWidth * (0.5 + leftPoint.x /2)), (float) (screenWidth * (0.5 + leftPoint.y / 2)), 2.5f, panelPaint);
//        }
//
//        if (rightPoint != null) {
//            panelPaint.setColor(0x5f0000ff);
//            canvas.drawCircle((float) (screenWidth * (0.5 + rightPoint.x /2)), (float) (screenWidth * (0.5 + rightPoint.y / 2)), 2.5f, panelPaint);
//        }


        drawStickman(canvas, game.getLeftTool(), game.getRightTool());

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

        game.quitButton.draw(canvas);
    }

    private void drawStickman(Canvas canvas, Tool toolLeft, Tool toolRight) {
        Paint paint = paints.gridPaint;
        PointF lPoint = toolLeft.getPosition();
        PointF rPoint = toolRight.getPosition();
        PointF lDelPoint = toolLeft.getDelayedPosition();
        PointF rDelPoint = toolRight.getDelayedPosition();

        float size = screenWidth * 0.2f;
        float x0 = screenWidth * 0.5f;
        float y0 = inputArea.top;
        float xOfs = (lPoint.x + rPoint.x) * size * 0.5f;
        float yOfs = (lPoint.y + rPoint.y) * size * 0.5f;

        float waistX = x0 + xOfs * 0.1f;
        float waistY = y0 -size * 0.5f + yOfs * 0.05f;
        float neckX = x0 + xOfs * 0.2f;
        float neckY = y0 -size * 0.8f + yOfs * 0.1f;
        float headR = size * 0.1f;
        float headY = neckY - size * 0.1f;

        float lFootX = x0 - size * 0.1f;
        float rFootX = x0 + size * 0.1f;
        float lKneeX = x0 - size * 0.1f + xOfs * 0.1f;
        float rKneeX = x0 + size * 0.1f + xOfs * 0.1f;
        float kneeY = y0 -size * 0.25f + yOfs * 0.02f;

        float lShoulderX = neckX - size * 0.1f;
        float lShoulderY = neckY + size * 0.05f - xOfs * 0.05f;
        float rShoulderX = neckX + size * 0.1f;
        float rShoulderY = neckY + size * 0.05f + xOfs * 0.05f;

        float lFistX = lShoulderX + lPoint.x * size * 0.2f - size * 0.1f;
        float lFistY = lShoulderY + lPoint.y * size * 0.2f + size * 0.1f;

        float rFistX = rShoulderX + rPoint.x * size * 0.2f + size * 0.1f;
        float rFistY = rShoulderY + rPoint.y * size * 0.2f + size * 0.1f;

        float lElbowX = lShoulderX + lPoint.x * size * 0.1f - size * 0.1f;
        float lElbowY = lFistY * 0.5f + lShoulderY * 0.5f + size * 0.1f;

        float rElbowX = rShoulderX + rPoint.x * size * 0.1f + size * 0.1f;
        float rElbowY = rFistY * 0.5f + rShoulderY * 0.5f + size * 0.1f;

        float lTipX = lShoulderX + lPoint.x * size * 0.6f - size * 0.1f;
        float lTipY = lShoulderY + lPoint.y * size * 0.7f - size * 0.01f;

        float rTipX = rShoulderX + rPoint.x * size * 0.6f + size * 0.1f;
        float rTipY = rShoulderY + rPoint.y * size * 0.7f - size * 0.01f;

        float lDTipX = lShoulderX + lDelPoint.x * size * 0.6f - size * 0.1f;
        float lDTipY = lShoulderY + lDelPoint.y * size * 0.7f - size * 0.01f;

        float rDTipX = rShoulderX + rDelPoint.x * size * 0.6f + size * 0.1f;
        float rDTipY = rShoulderY + rDelPoint.y * size * 0.7f - size * 0.01f;

        paints.toolPaint.setStyle(Paint.Style.STROKE);
        paints.toolPaint.setStrokeWidth(4);

        paints.toolPaint.setColor(paints.leftTargetPaint.getColor());
        canvas.drawLine(lDTipX, lDTipY, lTipX, lTipY, paints.toolPaint);

        paints.toolPaint.setColor(paints.rightTargetPaint.getColor());
        canvas.drawLine(rDTipX, rDTipY, rTipX, rTipY, paints.toolPaint);

        paints.toolPaint.setStrokeWidth(6);

        paints.toolPaint.setColor(paints.leftTargetPaint.getColor());
        canvas.drawLine(lTipX, lTipY, lFistX, lFistY, paints.toolPaint);

        paints.toolPaint.setColor(paints.rightTargetPaint.getColor());
        canvas.drawLine(rTipX, rTipY, rFistX, rFistY, paints.toolPaint);

        paints.toolPaint.setStrokeWidth(2);

        paints.toolPaint.setColor(paints.targetHilightPaint.getColor());
        canvas.drawLine(lTipX, lTipY, lFistX, lFistY, paints.toolPaint);

        paints.toolPaint.setColor(paints.targetHilightPaint.getColor());
        canvas.drawLine(rTipX, rTipY, rFistX, rFistY, paints.toolPaint);

        canvas.drawLine(lKneeX, kneeY, lFootX, y0, paint);
        canvas.drawLine(lKneeX, kneeY, waistX, waistY, paint);
        canvas.drawLine(rKneeX, kneeY, rFootX, y0, paint);
        canvas.drawLine(rKneeX, kneeY, waistX, waistY, paint);

        canvas.drawLine(neckX, neckY, waistX, waistY, paint);

        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint);

        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint);
        canvas.drawLine(lElbowX, lElbowY, lFistX, lFistY, paint);

        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint);
        canvas.drawLine(rElbowX, rElbowY, rFistX, rFistY, paint);

        canvas.drawCircle(neckX, headY, headR, paint);
    }

    private void drawTool(Canvas canvas, Tool tool, Paint paint) {
        float boxSize = getBoxSize();

        PointF position = tool.getPosition();
        PointF delayed = tool.getDelayedPosition();
        PointF start = tool.getStartPoint();
        Point direction = tool.getDirection();

        float pX = (float) (boxSize * position.x);
        float pY = (float) (boxSize * position.y);

        float dpX = (float) (boxSize * delayed.x);
        float dpY = (float) (boxSize * delayed.y);

        float sX = (float) (boxSize * start.x);
        float sY = (float) (boxSize * start.y);

//        canvas.drawCircle(pX, pY, 10, panelPaint);
//        canvas.drawRect(dpX - 10, dpY - 10, dpX + 10, dpY + 10, panelPaint);
        canvas.drawLine(dpX, dpY, pX, pY, paint);
//        canvas.drawRect(sX - 10, sY - 10, sX + 10, sY + 10, panelPaint);

//        canvas.drawLine(pX, pY, pX + direction.x * 100, pY + direction.y *100, panelPaint);
    }

    private void drawToolHit(Canvas canvas, ToolHit toolHit) {
        float boxSize = getBoxSize();

        Paint paint = paints.targetPaint;
        paint.setStrokeWidth(2);
        paint.setColor(ColorPalette.fade(0xffffff, (float) (1 - toolHit.getAge() / ToolHit.MAX_AGE)));

        canvas.drawLine(
                toolHit.getStartPoint().x * boxSize,
                toolHit.getStartPoint().y * boxSize,
                toolHit.getEndPoint().x * boxSize,
                toolHit.getEndPoint().y * boxSize,
                paint);
    }

    private float getBoxSize() {
        return playArea.width() * boxSizeToScreenFactor;
    }

    private void drawTarget(Canvas canvas, Target target, double windowDuration) {
        float boxSize = getBoxSize();

        float x = target.getxOffset() * boxSize * 1;
        float y = target.getyOffset() * boxSize * 1;

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

        if (target.isHit()) {
            drawHitTargetBox(canvas, target, boxSize, percent);
        } else {
            drawTargetBox(canvas, target, boxSize, percent);

//            drawTargetAnticipationHint2(canvas, target, boxSize, percent);
//            drawTargetAnticipationHint(canvas, target, percent);
        }

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

    private void drawTargetBox(Canvas canvas, Target target, float boxSize, double percent) {
        Paint paint = paints.targetPaint;

        if (target.isMiss()) {
            paint.setColor(paints.targetMissedPaint.getColor());
        }
        else if (target.getSide() == Target.SIDE_LEFT) {
            paint.setColor(target.isActive() ? paints.leftActiveTargetPaint.getColor() : paints.leftTargetPaint.getColor());
        } else if (target.getSide() == Target.SIDE_RIGHT) {
            paint.setColor(target.isActive() ? paints.rightActiveTargetPaint.getColor() : paints.rightTargetPaint.getColor());
        }

        canvas.drawPath(targetBoxPath, paint);

        if (!target.isActive() && !target.isMiss()) {
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
    }

    private void drawHitTargetBox(Canvas canvas, Target target, float boxSize, double percent) {
        double hitTime = target.getHitTime();
        if (hitTime > 0.25) {
            return;
        }
        double factor = hitTime / 0.3;

        Paint paint = paints.targetPaint;
        paint.setStrokeWidth(10);

        if (target.getSide() == Target.SIDE_LEFT) {
            paint.setColor(paints.leftActiveTargetPaint.getColor());
        } else if (target.getSide() == Target.SIDE_RIGHT) {
            paint.setColor(paints.rightActiveTargetPaint.getColor());
        }

        float scale = (float) (1 - factor);

        canvas.save();
        canvas.translate(0, (float) (-hitTime * boxSize));
        canvas.scale(scale, scale);
        canvas.rotate((float) (factor * 30));
        canvas.clipRect(-boxSize * 0.5f, -boxSize * 0.5f, boxSize * 0.5f, 0);
        canvas.drawPath(targetBoxPath, paint);
        canvas.drawPath(targetArrowPath, paints.targetHilightPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, (float) (hitTime * boxSize));
        canvas.scale(scale, scale);
        canvas.rotate((float) (-factor * 30));
        canvas.clipRect(-boxSize * 0.5f, 0, boxSize * 0.5f, boxSize * 0.5f);
        canvas.drawPath(targetBoxPath, paint);
        canvas.drawPath(targetArrowPath, paints.targetHilightPaint);
        canvas.restore();
    }

    private void drawTargetAnticipationHint2(Canvas canvas, Target target, float boxSize, double percent) {
        if (percent < 0) {
            return;
        }
        Paint paint = paints.targetPaint;
        paint.setStrokeWidth(2);

        if (target.getSide() == Target.SIDE_LEFT) {
            paint.setColor(paints.leftTargetPaint.getColor());
        } else if (target.getSide() == Target.SIDE_RIGHT) {
            paint.setColor(paints.rightTargetPaint.getColor());
        }

        canvas.drawLine(
                (float)(-boxSize * percent * 0.25 - boxSize * 0.05),
                0,
                (float)(-boxSize * percent * 0.25 - boxSize * (0.2 + 0.05)),
                boxSize * 0.2f,
                paint);

        canvas.drawLine(
                (float)(-boxSize * percent * 0.25 - boxSize * 0.05),
                0,
                (float)(-boxSize * percent * 0.25 - boxSize * (0.2 + 0.05)),
                -boxSize * 0.2f,
                paint);
    }

    private void drawTargetAnticipationHint(Canvas canvas, Target target, double percent) {
        percent = 1 - percent;
        final double minPercent = 0.8;
        if (percent < minPercent || percent > 1) {
            return;
        }

        Paint paint = paints.targetPaint;

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
        path.lineTo(- halfSize * (margin - wmargin), 0);
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

        game.quitButton.resize(width, height);
    }

    public RectF getInputArea() {
        return inputArea;
    }

}
