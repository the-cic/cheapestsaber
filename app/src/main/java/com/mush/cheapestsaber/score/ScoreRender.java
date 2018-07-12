package com.mush.cheapestsaber.score;

import android.graphics.Canvas;

import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.common.PaintPalette;


/**
 * Created by mush on 10/07/2018.
 */
public class ScoreRender extends StateRender {

    private PaintPalette paints;
    private ScoreMain score;

    public ScoreRender(ScoreMain main) {
        paints = new PaintPalette();
        score = main;
    }

    public void draw(Canvas canvas) {
        int width = 300;
        int height =  200;
        float scale = (float)screenWidth / width;
        scale = scale > 0 ? scale : 1;
        int fullHeight = (int) (screenHeight / scale);
        int offset = (fullHeight - height) / 2;
        offset = offset > 0 ? offset : 0;

        canvas.save();
        canvas.scale(scale, scale);
        canvas.translate(0, offset);

        canvas.drawRoundRect(10, 10, 290, 190, 20, 20, paints.gridPaint);
        canvas.drawText("Hit: " + score.hitCount, 50, 50, paints.labelPaint);
        canvas.drawText("Missed: " + (score.totalCount - score.hitCount), 50, 80, paints.labelPaint);
        canvas.drawText("Best combo: " + score.maxComboLength, 50, 110, paints.labelPaint);

        canvas.drawText("OK", 130, 160, paints.labelPaint);

        canvas.restore();
    }

}
