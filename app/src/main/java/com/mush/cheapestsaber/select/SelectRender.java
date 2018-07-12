package com.mush.cheapestsaber.select;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mush.cheapestsaber.common.ColorPalette;
import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.common.PaintPalette;


/**
 * Created by mush on 10/07/2018.
 */
public class SelectRender extends StateRender {

    private PaintPalette paints;
    private SelectMain main;
    private Paint transitionPaint;

    public SelectRender(SelectMain selectMain) {
        paints = new PaintPalette();
        this.main = selectMain;
        transitionPaint = new Paint();
        transitionPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas) {
        main.panel.draw(canvas);
        main.startButton.draw(canvas);

        if (main.transition > 0) {
            transitionPaint.setColor(ColorPalette.fade(ColorPalette.BACKGROUND, main.transition));
            canvas.drawRect(main.panel.drawArea, transitionPaint);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        main.panel.resize(width, height);
        main.startButton.resize(width, height);
    }
}
