package org.yndongyong.fastandroid.view.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import org.yndongyong.fastandroid.R;


/**
 * Created by Dong on 2016/5/15.
 */
public class CircularProgressDrawable extends Drawable implements Animatable {
    private static final ArgbEvaluator COLOR_EVALUATOR = new ArgbEvaluator();
    public static final Interpolator END_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator DEFAULT_ROTATION_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator DEFAULT_SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ROTATION_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    private static final int END_ANIMATOR_DURATION = 200;
    private final RectF fBounds;
    private ValueAnimator mSweepAppearingAnimator;
    private ValueAnimator mSweepDisappearingAnimator;
    private ValueAnimator mRotationAnimator;
    private ValueAnimator mEndAnimator;
    private CircularProgressDrawable.OnEndListener mOnEndListener;
    private boolean mModeAppearing;
    private Paint mPaint;
    private boolean mRunning;
    private int mCurrentColor;
    private int mCurrentIndexColor;
    private float mCurrentSweepAngle;
    private float mCurrentRotationAngleOffset;
    private float mCurrentRotationAngle;
    private float mCurrentEndRatio;
    private Interpolator mAngleInterpolator;
    private Interpolator mSweepInterpolator;
    private float mBorderWidth;
    private int[] mColors;
    private float mSweepSpeed;
    private float mRotationSpeed;
    private int mMinSweepAngle;
    private int mMaxSweepAngle;
    private boolean mFirstSweepAnimation;

    private CircularProgressDrawable(int[] colors, float borderWidth, float sweepSpeed, float rotationSpeed, int minSweepAngle, int maxSweepAngle, CircularProgressDrawable.Style style, Interpolator angleInterpolator, Interpolator sweepInterpolator) {
        this.fBounds = new RectF();
        this.mCurrentRotationAngleOffset = 0.0F;
        this.mCurrentRotationAngle = 0.0F;
        this.mCurrentEndRatio = 1.0F;
        this.mSweepInterpolator = sweepInterpolator;
        this.mAngleInterpolator = angleInterpolator;
        this.mBorderWidth = borderWidth;
        this.mCurrentIndexColor = 0;
        this.mColors = colors;
        this.mCurrentColor = this.mColors[0];
        this.mSweepSpeed = sweepSpeed;
        this.mRotationSpeed = rotationSpeed;
        this.mMinSweepAngle = minSweepAngle;
        this.mMaxSweepAngle = maxSweepAngle;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(android.graphics.Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(borderWidth);
        this.mPaint.setStrokeCap(style == CircularProgressDrawable.Style.ROUNDED ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        this.mPaint.setColor(this.mColors[0]);
        this.setupAnimations();
    }

    private void reinitValues() {
        this.mFirstSweepAnimation = true;
        this.mCurrentEndRatio = 1.0F;
        this.mPaint.setColor(this.mCurrentColor);
    }

    public void draw(Canvas canvas) {
        float startAngle = this.mCurrentRotationAngle - this.mCurrentRotationAngleOffset;
        float sweepAngle = this.mCurrentSweepAngle;
        if (!this.mModeAppearing) {
            startAngle += 360.0F - sweepAngle;
        }

        startAngle %= 360.0F;
        if (this.mCurrentEndRatio < 1.0F) {
            float newSweepAngle = sweepAngle * this.mCurrentEndRatio;
            startAngle = (startAngle + (sweepAngle - newSweepAngle)) % 360.0F;
            sweepAngle = newSweepAngle;
        }

        canvas.drawArc(this.fBounds, startAngle, sweepAngle, false, this.mPaint);
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return -3;
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.fBounds.left = (float) bounds.left + this.mBorderWidth / 2.0F + 0.5F;
        this.fBounds.right = (float) bounds.right - this.mBorderWidth / 2.0F - 0.5F;
        this.fBounds.top = (float) bounds.top + this.mBorderWidth / 2.0F + 0.5F;
        this.fBounds.bottom = (float) bounds.bottom - this.mBorderWidth / 2.0F - 0.5F;
    }

    private void setAppearing() {
        this.mModeAppearing = true;
        this.mCurrentRotationAngleOffset += (float) this.mMinSweepAngle;
    }

    private void setDisappearing() {
        this.mModeAppearing = false;
        this.mCurrentRotationAngleOffset += (float) (360 - this.mMaxSweepAngle);
    }

    private void setupAnimations() {
        this.mRotationAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 360.0F});
        this.mRotationAnimator.setInterpolator(this.mAngleInterpolator);
        this.mRotationAnimator.setDuration((long) (2000.0F / this.mRotationSpeed));
        this.mRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = animation.getAnimatedFraction() * 360.0F;
                CircularProgressDrawable.this.setCurrentRotationAngle(angle);
            }
        });
        this.mRotationAnimator.setRepeatCount(-1);
        this.mRotationAnimator.setRepeatMode(1);
        this.mSweepAppearingAnimator = ValueAnimator.ofFloat(new float[]{(float) this.mMinSweepAngle, (float) this.mMaxSweepAngle});
        this.mSweepAppearingAnimator.setInterpolator(this.mSweepInterpolator);
        this.mSweepAppearingAnimator.setDuration((long) (600.0F / this.mSweepSpeed));
        this.mSweepAppearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                float angle;
                if (CircularProgressDrawable.this.mFirstSweepAnimation) {
                    angle = animatedFraction * (float) CircularProgressDrawable.this.mMaxSweepAngle;
                } else {
                    angle = (float) CircularProgressDrawable.this.mMinSweepAngle + animatedFraction * (float) (CircularProgressDrawable.this.mMaxSweepAngle - CircularProgressDrawable.this.mMinSweepAngle);
                }

                CircularProgressDrawable.this.setCurrentSweepAngle(angle);
            }
        });
        this.mSweepAppearingAnimator.addListener(new Animator.AnimatorListener() {
            boolean cancelled = false;

            public void onAnimationStart(Animator animation) {
                this.cancelled = false;
                CircularProgressDrawable.this.mModeAppearing = true;
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.cancelled) {
                    CircularProgressDrawable.this.mFirstSweepAnimation = false;
                    CircularProgressDrawable.this.setDisappearing();
                    CircularProgressDrawable.this.mSweepDisappearingAnimator.start();
                }

            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mSweepDisappearingAnimator = ValueAnimator.ofFloat(new float[]{(float) this.mMaxSweepAngle, (float) this.mMinSweepAngle});
        this.mSweepDisappearingAnimator.setInterpolator(this.mSweepInterpolator);
        this.mSweepDisappearingAnimator.setDuration((long) (600.0F / this.mSweepSpeed));
        this.mSweepDisappearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                CircularProgressDrawable.this.setCurrentSweepAngle((float) CircularProgressDrawable.this.mMaxSweepAngle - animatedFraction * (float) (CircularProgressDrawable.this.mMaxSweepAngle - CircularProgressDrawable.this.mMinSweepAngle));
                long duration = animation.getDuration();
                long played = animation.getCurrentPlayTime();
                float fraction = (float) played / (float) duration;
                if (CircularProgressDrawable.this.mColors.length > 1 && fraction > 0.7F) {
                    int prevColor = CircularProgressDrawable.this.mCurrentColor;
                    int nextColor = CircularProgressDrawable.this.mColors[(CircularProgressDrawable.this.mCurrentIndexColor + 1) % CircularProgressDrawable.this.mColors.length];
                    int newColor = ((Integer) CircularProgressDrawable.COLOR_EVALUATOR.evaluate((fraction - 0.7F) / 0.3F, Integer.valueOf(prevColor), Integer.valueOf(nextColor))).intValue();
                    CircularProgressDrawable.this.mPaint.setColor(newColor);
                }

            }
        });
        this.mSweepDisappearingAnimator.addListener(new Animator.AnimatorListener() {
            boolean cancelled;

            public void onAnimationStart(Animator animation) {
                this.cancelled = false;
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.cancelled) {
                    CircularProgressDrawable.this.setAppearing();
                    CircularProgressDrawable.this.mCurrentIndexColor = (CircularProgressDrawable.this.mCurrentIndexColor + 1) % CircularProgressDrawable.this.mColors.length;
                    CircularProgressDrawable.this.mCurrentColor = CircularProgressDrawable.this.mColors[CircularProgressDrawable.this.mCurrentIndexColor];
                    CircularProgressDrawable.this.mPaint.setColor(CircularProgressDrawable.this.mCurrentColor);
                    CircularProgressDrawable.this.mSweepAppearingAnimator.start();
                }

            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mEndAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F});
        this.mEndAnimator.setInterpolator(END_INTERPOLATOR);
        this.mEndAnimator.setDuration(200L);
        this.mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CircularProgressDrawable.this.setEndRatio(1.0F - animation.getAnimatedFraction());
            }
        });
        this.mEndAnimator.addListener(new Animator.AnimatorListener() {
            private boolean cancelled;

            public void onAnimationStart(Animator animation) {
                this.cancelled = false;
            }

            public void onAnimationEnd(Animator animation) {
                CircularProgressDrawable.this.setEndRatio(0.0F);
                if (!this.cancelled) {
                    CircularProgressDrawable.this.stop();
                }

            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void start() {
        if (!this.isRunning()) {
            this.mRunning = true;
            this.reinitValues();
            this.mRotationAnimator.start();
            this.mSweepAppearingAnimator.start();
            this.invalidateSelf();
        }
    }

    public void stop() {
        if (this.isRunning()) {
            this.mRunning = false;
            this.stopAnimators();
            this.invalidateSelf();
        }
    }

    private void stopAnimators() {
        this.mRotationAnimator.cancel();
        this.mSweepAppearingAnimator.cancel();
        this.mSweepDisappearingAnimator.cancel();
        this.mEndAnimator.cancel();
    }

    public void progressiveStop(CircularProgressDrawable.OnEndListener listener) {
        if (this.isRunning() && !this.mEndAnimator.isRunning()) {
            this.mOnEndListener = listener;
            this.mEndAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    CircularProgressDrawable.this.mEndAnimator.removeListener(this);
                    if (CircularProgressDrawable.this.mOnEndListener != null) {
                        CircularProgressDrawable.this.mOnEndListener.onEnd(CircularProgressDrawable.this);
                    }

                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            this.mEndAnimator.start();
        }
    }

    public void progressiveStop() {
        this.progressiveStop((CircularProgressDrawable.OnEndListener) null);
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public void setCurrentRotationAngle(float currentRotationAngle) {
        this.mCurrentRotationAngle = currentRotationAngle;
        this.invalidateSelf();
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        this.mCurrentSweepAngle = currentSweepAngle;
        this.invalidateSelf();
    }

    private void setEndRatio(float ratio) {
        this.mCurrentEndRatio = ratio;
        this.invalidateSelf();
    }

    public static class Builder {
        private int[] mColors;
        private float mSweepSpeed;
        private float mRotationSpeed;
        private float mStrokeWidth;
        private int mMinSweepAngle;
        private int mMaxSweepAngle;
        private CircularProgressDrawable.Style mStyle;
        private Interpolator mSweepInterpolator;
        private Interpolator mAngleInterpolator;

        public Builder(Context context) {
            this(context, false);
        }

        public Builder(Context context, boolean editMode) {
            this.mSweepInterpolator = CircularProgressDrawable.DEFAULT_SWEEP_INTERPOLATOR;
            this.mAngleInterpolator = CircularProgressDrawable.DEFAULT_ROTATION_INTERPOLATOR;
            this.initValues(context, editMode);
        }

        private void initValues(Context context, boolean editMode) {
            this.mStrokeWidth = context.getResources().getDimension(R.dimen
                    .cpb_default_stroke_width);
            this.mSweepSpeed = 1.0F;
            this.mRotationSpeed = 1.0F;
            if (editMode) {
                this.mColors = new int[]{-16776961};
                this.mMinSweepAngle = 20;
                this.mMaxSweepAngle = 300;
            } else {
                this.mColors = new int[]{context.getResources().getColor(R.color
                        .cpb_default_color)};
                this.mMinSweepAngle = context.getResources().getInteger
                        (R.integer.cpb_default_min_sweep_angle);
                this.mMaxSweepAngle = context.getResources().getInteger(R.integer
                        .cpb_default_max_sweep_angle);
            }

            this.mStyle = CircularProgressDrawable.Style.ROUNDED;
        }

        public CircularProgressDrawable.Builder color(int color) {
            this.mColors = new int[]{color};
            return this;
        }

        public CircularProgressDrawable.Builder colors(int[] colors) {
            CircularProgressBarUtils.checkColors(colors);
            this.mColors = colors;
            return this;
        }

        public CircularProgressDrawable.Builder sweepSpeed(float sweepSpeed) {
            CircularProgressBarUtils.checkSpeed(sweepSpeed);
            this.mSweepSpeed = sweepSpeed;
            return this;
        }

        public CircularProgressDrawable.Builder rotationSpeed(float rotationSpeed) {
            CircularProgressBarUtils.checkSpeed(rotationSpeed);
            this.mRotationSpeed = rotationSpeed;
            return this;
        }

        public CircularProgressDrawable.Builder minSweepAngle(int minSweepAngle) {
            CircularProgressBarUtils.checkAngle(minSweepAngle);
            this.mMinSweepAngle = minSweepAngle;
            return this;
        }

        public CircularProgressDrawable.Builder maxSweepAngle(int maxSweepAngle) {
            CircularProgressBarUtils.checkAngle(maxSweepAngle);
            this.mMaxSweepAngle = maxSweepAngle;
            return this;
        }

        public CircularProgressDrawable.Builder strokeWidth(float strokeWidth) {
            CircularProgressBarUtils.checkPositiveOrZero(strokeWidth, "StrokeWidth");
            this.mStrokeWidth = strokeWidth;
            return this;
        }

        public CircularProgressDrawable.Builder style(CircularProgressDrawable.Style style) {
            CircularProgressBarUtils.checkNotNull(style, "Style");
            this.mStyle = style;
            return this;
        }

        public CircularProgressDrawable.Builder sweepInterpolator(Interpolator interpolator) {
            CircularProgressBarUtils.checkNotNull(interpolator, "Sweep interpolator");
            this.mSweepInterpolator = interpolator;
            return this;
        }

        public CircularProgressDrawable.Builder angleInterpolator(Interpolator interpolator) {
            CircularProgressBarUtils.checkNotNull(interpolator, "Angle interpolator");
            this.mAngleInterpolator = interpolator;
            return this;
        }

        public CircularProgressDrawable build() {
            return new CircularProgressDrawable(this.mColors, this.mStrokeWidth,
                    this.mSweepSpeed, this.mRotationSpeed,
                    this.mMinSweepAngle, this.mMaxSweepAngle,
                    this.mStyle, this.mAngleInterpolator,
                    this.mSweepInterpolator);
        }
    }

    public interface OnEndListener {
        void onEnd(CircularProgressDrawable var1);
    }

    public static enum Style {
        NORMAL,
        ROUNDED;

        private Style() {
        }
    }
}
