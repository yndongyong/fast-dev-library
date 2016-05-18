package org.yndongyong.fastandroid.view.progress;

import android.animation.ValueAnimator;

/**
 * Created by Dong on 2016/5/15.
 */
public class CircularProgressBarUtils {
    static void checkSpeed(float speed) {
        if (speed <= 0.0F)
            throw new IllegalArgumentException("Speed must be >= 0");
    }

    static void checkColors(int[] colors) {
        if ((colors == null) || (colors.length == 0))
            throw new IllegalArgumentException("You must provide at least 1 color");
    }

    static void checkAngle(int angle) {
        if ((angle < 0) || (angle > 360))
            throw new IllegalArgumentException(String.format("Illegal angle %d: must be >=0 and <= 360", new Object[]{Integer.valueOf(angle)}));
    }

    static void checkPositiveOrZero(float number, String name) {
        if (number < 0.0F)
            throw new IllegalArgumentException(String.format("%s %d must be positive", new Object[]{name, Float.valueOf(number)}));
    }

    static void checkPositive(int number, String name) {
        if (number <= 0)
            throw new IllegalArgumentException(String.format("%s must not be null", new Object[]{name}));
    }

    static void checkNotNull(Object o, String name) {
        if (o == null)
            throw new IllegalArgumentException(String.format("%s must be not null", new Object[]{name}));
    }

    static float getAnimatedFraction(ValueAnimator animator) {
        float fraction = animator.getDuration() > 0L ? (float) animator.getCurrentPlayTime() / (float) animator.getDuration() : 0.0F;

        fraction = Math.min(fraction, 1.0F);
        fraction = animator.getInterpolator().getInterpolation(fraction);
        return fraction;
    }
}
