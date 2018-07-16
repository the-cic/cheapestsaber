package com.mush.cheapestsaber.score;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.mush.cheapestsaber.common.ColorPalette;
import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.common.PaintPalette;


/**
 * Created by mush on 10/07/2018.
 */
public class ScoreRender extends StateRender {

    private PaintPalette paints;
    private ScoreMain main;
    private Paint transitionPaint;

    public ScoreRender(ScoreMain score) {
        paints = new PaintPalette();
        this.main = score;
        transitionPaint = new Paint();
        transitionPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas) {
        main.uiElements.draw(canvas);

        if (main.transition > 0) {
            transitionPaint.setColor(ColorPalette.fade(ColorPalette.BACKGROUND, main.transition));
            canvas.drawRect(main.panel.getDrawArea(), transitionPaint);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        main.uiElements.resize(width, height);
    }

}
