package com.mush.cheapestsaber.game;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by mush on 06/07/2018.
 */
public class Tool {

    private PointF position;
    private PointF delayedPosition;
    private PointF rawVelocity;
    private PointF startPoint;
    private Point direction;
    private boolean active;

    public Tool() {
        position = new PointF();
        delayedPosition = new PointF();
        rawVelocity = new PointF();
        startPoint = new PointF();
        direction = new Point();
        active = false;
    }

    public void update(PointF point, double seconds) {
        if (point != null) {
            updatePosition(point);
            updateDirection();

            if (!active) {
                active = true;
//                resetPosition(point);
                delayedPosition.set(point);
            }
        } else {
            active = false;
        }

        float dx = (position.x - delayedPosition.x) * 10;
        float dy = (position.y - delayedPosition.y) * 10;
        float d = (float) Math.sqrt(dx * dx + dy * dy);
        boolean small = d < 1;

        dx = small ? dx : dx / d;
        dy = small ? dy : dy / d;

        dx *= 5 * seconds;
        dy *= 5 * seconds;

        delayedPosition.offset(dx, dy);
    }

//    private void resetPosition(PointF point) {
//        if (point != null) {
//            position.set(point);
//            delayedPosition.set(point);
//        }
//    }

    public void resetStartPoint() {
        startPoint.set(position);
        direction.set(0, 0);
    }

    private void updatePosition(PointF point) {
        float vx = point.x - position.x;
        float vy = point.y - position.y;

        if (Math.signum(vx) != Math.signum(rawVelocity.x)) {
            startPoint.x = point.x;
        }

        if (Math.signum(vy) != Math.signum(rawVelocity.y)) {
            startPoint.y = point.y;
        }

        rawVelocity.set(vx, vy);
        position.set(point);
    }

    private void updateDirection() {
        float dx = position.x - startPoint.x;
        float dy = position.y - startPoint.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        if (d > 0.15) {
            dx /= d;
            dy /= d;

            // 0.383 = sin(pi/8)
            int idx = Math.abs(dx) > 0.383 ? (int)Math.signum(dx) : 0;
            int idy = Math.abs(dy) > 0.383 ? (int)Math.signum(dy) : 0;

            direction.set(idx, idy);
        } else {
            direction.set(0, 0);
        }
    }

    public PointF getDelayedPosition() {
        return delayedPosition;
    }

    public PointF getPosition() {
        return position;
    }

    public PointF getStartPoint() {
        return startPoint;
    }

    public Point getDirection() {
        return direction;
    }
}
