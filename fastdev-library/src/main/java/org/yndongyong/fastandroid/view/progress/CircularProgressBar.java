package org.yndongyong.fastandroid.view.progress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import org.yndongyong.fastandroid.R;


/**
 * Created by Dong on 2016/5/15.
 */
public class CircularProgressBar extends ProgressBar {
    public CircularProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
//        this(context, attrs, android.R.attr.cpbStyle);
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (this.isInEditMode()) {
            this.setIndeterminateDrawable((new CircularProgressDrawable.Builder(context, true)).build());
        } else {
            Resources res = context.getResources();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar,
                    defStyle, 0);
            int color = a.getColor(R.styleable.CircularProgressBar_cpb_color, res.getColor(R.color
                    .cpb_default_color));
            float strokeWidth = a.getDimension(R.styleable.CircularProgressBar_cpb_stroke_width,
                    res.getDimension(R.dimen.cpb_default_stroke_width));
            float sweepSpeed = a.getFloat(R.styleable.CircularProgressBar_cpb_sweep_speed, Float
                    .parseFloat(res.getString(R.string.cpb_default_sweep_speed)));
            float rotationSpeed = a.getFloat(R.styleable.CircularProgressBar_cpb_rotation_speed, Float.parseFloat(res.getString(R.string.cpb_default_rotation_speed)));
            int colorsId = a.getResourceId(R.styleable.CircularProgressBar_cpb_colors, 0);
            int minSweepAngle = a.getInteger(R.styleable.CircularProgressBar_cpb_min_sweep_angle, res.getInteger(R.integer.cpb_default_min_sweep_angle));
            int maxSweepAngle = a.getInteger(R.styleable.CircularProgressBar_cpb_max_sweep_angle, res.getInteger(R.integer.cpb_default_max_sweep_angle));
            a.recycle();
            int[] colors = null;
            if (colorsId != 0) {
                colors = res.getIntArray(colorsId);
            }

            CircularProgressDrawable.Builder builder = (new CircularProgressDrawable.Builder(context)).sweepSpeed(sweepSpeed).rotationSpeed(rotationSpeed).strokeWidth(strokeWidth).minSweepAngle(minSweepAngle).maxSweepAngle(maxSweepAngle);
            if (colors != null && colors.length > 0) {
                builder.colors(colors);
            } else {
                builder.color(color);
            }

            CircularProgressDrawable indeterminateDrawable = builder.build();
            this.setIndeterminateDrawable(indeterminateDrawable);
        }
    }

    private CircularProgressDrawable checkIndeterminateDrawable() {
        Drawable ret = this.getIndeterminateDrawable();
        if (ret != null && ret instanceof CircularProgressDrawable) {
            return (CircularProgressDrawable) ret;
        } else {
            throw new RuntimeException("The drawable is not a CircularProgressDrawable");
        }
    }

    public void progressiveStop() {
        this.checkIndeterminateDrawable().progressiveStop();
    }

    public void progressiveStop(CircularProgressDrawable.OnEndListener listener) {
        this.checkIndeterminateDrawable().progressiveStop(listener);
    }

}
