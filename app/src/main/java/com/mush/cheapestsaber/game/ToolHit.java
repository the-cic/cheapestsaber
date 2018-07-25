package com.mush.cheapestsaber.game;

import android.graphics.PointF;

/**
 * Created by mush on 24/07/2018.
 */
public class ToolHit {

    private PointF startPoint;
    private PointF endPoint;
    private double age;
    public static final double MAX_AGE = 0.1;

    public ToolHit(Tool tool) {
        this.startPoint = new PointF(tool.getStartPoint().x, tool.getStartPoint().y);
        float dx = tool.getPosition().x - startPoint.x;
        float dy = tool.getPosition().y - startPoint.y;
        float d = (float) Math.sqrt(dx * dx + dy * dy);
        endPoint = new PointF(startPoint.x, startPoint.y);
        if (d > 0) {
            endPoint.x += dx / d;
            endPoint.y += dy / d;
        }
        age = 0;
    }

    public void update(double seconds) {
        age += seconds;
    }

    public PointF getStartPoint() {
        return startPoint;
    }

    public PointF getEndPoint() {
        return endPoint;
    }

    public double getAge() {
        return age;
    }
}
