package com.mush.cheapestsaber.common;

import android.graphics.Color;

/**
 * Created by mush on 12/07/2018.
 */
public class ColorPalette {

    public static final int BACKGROUND = 0x000000;

    public static final int TEXT = 0xFFFFFF;
    public static final int PANEL = 0x888888;
    public static final int BUTTON = 0xAAAAAA;
    public static final int BUTTON_PRESSED = 0xBBBBBB;
    public static final int TEXT_DISABLED = 0xCCCCCC;

    public static final int TARGET_LEFT = 0xCF0000;
    public static final int TARGET_RIGHT = 0x0000CF;
    public static final int TARGET_HIT = 0x88FF88;
    public static final int TARGET_MISS = 0x885511;

    public static final int TARGET_ACTIVE_LEFT = lighten(TARGET_LEFT, 0.5f);
    public static final int TARGET_ACTIVE_RIGHT = lighten(TARGET_RIGHT, 0.5f);

    public static final int TARGET_OUTLINE_LEFT = multiply(TARGET_LEFT, 0.5f);
    public static final int TARGET_OUTLINE_RIGHT = multiply(TARGET_RIGHT, 0.5f);

    public static final int multiply(int color, float factor) {
        float r = Color.red(color) * factor;
        float g = Color.green(color) * factor;
        float b = Color.blue(color) * factor;

        return Color.rgb(
                r > 0xff ? 0xff : (int) r,
                g > 0xff ? 0xff : (int) g,
                b > 0xff ? 0xff : (int) b);
    }

    public static final int lighten(int color, float factor) {
        float r = Color.red(color) + factor * 0xff;
        float g = Color.green(color) + factor * 0xff;
        float b = Color.blue(color) + factor * 0xff;

        return Color.rgb(
                r > 0xff ? 0xff : (int) r,
                g > 0xff ? 0xff : (int) g,
                b > 0xff ? 0xff : (int) b);
    }

    public static int opaque(int rgb) {
        return Color.argb(0xff, Color.red(rgb), Color.green(rgb), Color.blue(rgb));
    }

    public static int fade(int rgb, float alpha) {
        return Color.argb((int) (0xff * alpha), Color.red(rgb), Color.green(rgb), Color.blue(rgb));
    }

}
