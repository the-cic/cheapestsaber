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
        main.panel.draw(canvas);
        main.okButton.draw(canvas);
        main.hitsLabel.draw(canvas);
        main.missesLabel.draw(canvas);
        main.comboLabel.draw(canvas);

        if (main.transition > 0) {
            transitionPaint.setColor(ColorPalette.fade(ColorPalette.BACKGROUND, main.transition));
            canvas.drawRect(main.panel.getDrawArea(), transitionPaint);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        main.panel.resize(width, height);
        main.okButton.resize(width, height);
        main.hitsLabel.resize(width, height);
        main.missesLabel.resize(width, height);
        main.comboLabel.resize(width, height);
    }

}
