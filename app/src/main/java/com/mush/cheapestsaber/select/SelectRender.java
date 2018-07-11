package com.mush.cheapestsaber.select;

import android.graphics.Canvas;

import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.game.PaintPalette;


/**
 * Created by mush on 10/07/2018.
 */
public class SelectRender extends StateRender {

    private PaintPalette paints;
    private SelectMain select;

    public SelectRender(SelectMain main) {
        paints = new PaintPalette();
        select = main;
    }

    public void draw(Canvas canvas) {
        int width = 400;
        int height =  300;
        float scale = (float)screenWidth / width;
        scale = scale > 0 ? scale : 1;
        int fullHeight = (int) (screenHeight / scale);
        int offset = (fullHeight - height) / 2;
        offset = offset > 0 ? offset : 0;

        canvas.save();
        canvas.scale(scale, scale);
        canvas.translate(0, offset);

        canvas.drawRect(10, 10, 390, 290, paints.gridPaint);
        canvas.drawText("Start", 50, 200, paints.labelPaint);

        canvas.restore();
    }

}
