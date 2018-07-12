package com.mush.cheapestsaber.common;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import static com.mush.cheapestsaber.common.ColorPalette.opaque;

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

        leftTargetPaint = makeFillPaint(opaque(ColorPalette.TARGET_LEFT));
        rightTargetPaint = makeFillPaint(opaque(ColorPalette.TARGET_RIGHT));
        leftTargetOutlinePaint = makeFillPaint(opaque(ColorPalette.TARGET_OUTLINE_LEFT));
        rightTargetOutlinePaint = makeFillPaint(opaque(ColorPalette.TARGET_OUTLINE_RIGHT));
        leftActiveTargetPaint = makeFillPaint(opaque(ColorPalette.TARGET_ACTIVE_LEFT));
        rightActiveTargetPaint = makeFillPaint(opaque(ColorPalette.TARGET_ACTIVE_RIGHT));
        targetMissedPaint = makeFillPaint(opaque(ColorPalette.TARGET_MISS));
        targetHitPaint = makeFillPaint(opaque(ColorPalette.TARGET_HIT));

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
