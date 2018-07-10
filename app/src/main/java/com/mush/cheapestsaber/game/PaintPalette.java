package com.mush.cheapestsaber.game;

import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by mush on 10/07/2018.
 */
public class PaintPalette {

    public Paint scorePaint;
    public Paint labelPaint;
    public Paint gridPaint;
    public Paint toolPaint;
    public Paint targetHilightPaint;
    public Paint targetStemPaint;
    public Paint leftTargetPaint;
    public Paint rightTargetPaint;
    public Paint leftTargetOutlinePaint;
    public Paint rightTargetOutlinePaint;
    public Paint leftActiveTargetPaint;
    public Paint rightActiveTargetPaint;
    public Paint targetHitPaint;
    public Paint targetMissedPaint;
    public Paint targetPaint;
    public Paint targetDestinationPaint;

    public PaintPalette() {
        scorePaint = makeFillPaint(0xffffffff);
        labelPaint = makeFillPaint(0xffffffff);
        gridPaint = makeFillPaint(0xff888888);
        toolPaint = makeFillPaint(0xff888888);
        targetHilightPaint = makeFillPaint(0xffffffff);
        targetHilightPaint.setStrokeWidth(3);
        targetStemPaint = makeFillPaint(0xff888888);
        targetPaint = makeFillPaint(0xffffff00);
        targetDestinationPaint = makeFillPaint(0xffffff00);
        leftTargetPaint = makeFillPaint(0xffcf0000);
        rightTargetPaint = makeFillPaint(0xff0000cf);
        leftTargetOutlinePaint = makeFillPaint(0xff800000);
        rightTargetOutlinePaint = makeFillPaint(0xff000080);
        leftActiveTargetPaint = makeFillPaint(0xffff8888);
        rightActiveTargetPaint = makeFillPaint(0xff8888ff);
        targetMissedPaint = makeFillPaint(0xff885511);
        targetHitPaint = makeFillPaint(0xff88ff88);

        Typeface fpsTypeface = Typeface.create("sans-serif", Typeface.BOLD);
        scorePaint.setTextSize(20);
        scorePaint.setTypeface(fpsTypeface);
        scorePaint.setTextAlign(Paint.Align.CENTER);

        labelPaint.setTextSize(20);
        labelPaint.setTypeface(fpsTypeface);
    }

    private Paint makeFillPaint(int color) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }

}
