package com.POSD.Win8View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
public class Win8View extends ImageView {
    private static final int SCALE_ADD_INIT = 6;
    private static final int SCALE_REDUCE_INIT = 0;
    private static final int SCALING = 1;
    private boolean isFinish;
    private int mCenterHeight;
    private int mCenterWidth;
    private int mHeight;
    private float mMinScale;
    private OnViewClickListener mOnViewClickListener;
    private Handler mScaleHandler;
    private int mWidth;

    class C01071 extends Handler {
        private int count = 0;
        private boolean isClicked;
        private Matrix matrix = new Matrix();
        private float f3s;

        C01071() {
        }

        public void handleMessage(Message msg) {
            this.matrix.set(Win8View.this.getImageMatrix());
            switch (msg.what) {
                case 0:
                    if (Win8View.this.isFinish) {
                        Win8View.this.isFinish = false;
                        this.count = 0;
                        this.f3s = (float) Math.sqrt(Math.sqrt((double) Win8View.this.mMinScale));
                        Win8View.this.beginScale(this.matrix, this.f3s);
                        Win8View.this.mScaleHandler.sendEmptyMessage(1);
                        return;
                    }
                    Win8View.this.mScaleHandler.sendEmptyMessage(0);
                    return;
                case 1:
                    Win8View.this.beginScale(this.matrix, this.f3s);
                    if (this.count < 4) {
                        Win8View.this.mScaleHandler.sendEmptyMessage(1);
                    } else {
                        Win8View.this.isFinish = true;
                        if (Win8View.this.mOnViewClickListener == null || this.isClicked) {
                            this.isClicked = false;
                        } else {
                            this.isClicked = true;
                            Win8View.this.mOnViewClickListener.onViewClick(Win8View.this);
                        }
                    }
                    this.count++;
                    return;
                case 6:
                    if (Win8View.this.isFinish) {
                        Win8View.this.isFinish = false;
                        this.count = 0;
                        this.f3s = (float) Math.sqrt(Math.sqrt((double) (1.0f / Win8View.this.mMinScale)));
                        Win8View.this.beginScale(this.matrix, this.f3s);
                        Win8View.this.mScaleHandler.sendEmptyMessage(1);
                        return;
                    }
                    Win8View.this.mScaleHandler.sendEmptyMessage(6);
                    return;
                default:
                    return;
            }
        }
    }

    public interface OnViewClickListener {
        void onViewClick(Win8View win8View);
    }

    public Win8View(Context context) {
        this(context, null);
    }

    public Win8View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Win8View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMinScale = 0.85f;
        this.isFinish = true;
        this.mScaleHandler = new C01071();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            this.mWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
            this.mHeight = (getHeight() - getPaddingTop()) - getPaddingBottom();
            this.mCenterWidth = this.mWidth / 2;
            this.mCenterHeight = this.mHeight / 2;
            ((BitmapDrawable) getDrawable()).setAntiAlias(true);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mScaleHandler.sendEmptyMessage(0);
                break;
            case 1:
                this.mScaleHandler.sendEmptyMessage(6);
                break;
        }
        return true;
    }

    protected void sleep(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void beginScale(Matrix matrix, float scale) {
        matrix.postScale(scale, scale, (float) this.mCenterWidth, (float) this.mCenterHeight);
        setImageMatrix(matrix);
    }

    public void setOnClickIntent(OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }
}
