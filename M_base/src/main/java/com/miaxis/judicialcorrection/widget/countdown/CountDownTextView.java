package com.miaxis.judicialcorrection.widget.countdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2020/9/10 16:57
 * @des
 * @updateAuthor
 * @updateDes
 */
public class CountDownTextView extends androidx.appcompat.widget.AppCompatTextView {


    @BindingAdapter("totalTime")
    public static void setTotalTime(CountDownTextView view, int time) {
        Timber.i("setTotalTime %d", time);
        view.setTotalTime(time);
    }


    public CountDownTextView(@NonNull Context context) {
        super(context);
    }

    public CountDownTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    private CountDownListener mCountDownListener;

    public void setCountDownListener(CountDownListener countDownListener) {
        this.mCountDownListener = countDownListener;
    }

    @SuppressLint("SetTextI18n")
    public void setTime(int time) {
        this.start = time;
        if (this.start > 0) {
            setText(this.start + "s");
            loop();
        } else {
            setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    public void setTotalTime(int time) {
        this.total = time;
        this.start = time;
        setText(this.start + "s");
        loop();
    }

    public void reset() {
        start = total;
    }

    private int start = 10;
    private int total = 10;

    private Handler mHandler;

    private void loop() {
        if (this.mHandler != null && this.mRunnable != null) {
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mHandler.postDelayed(this.mRunnable, 1000);
        }
    }

    private void stopLoop() {
        if (this.mHandler != null && this.mRunnable != null) {
            this.mHandler.removeCallbacks(this.mRunnable);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHandler = new Handler();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            start--;
            setText(start + "s");
            if (mCountDownListener != null) {
                mCountDownListener.onCountDownProgress(start);
            }
            if (start > 0) {
                loop();
            } else {
                if (mCountDownListener != null) {
                    mCountDownListener.onCountDownDone();
                }
            }
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            loop();
        } else {
            stopLoop();
        }
    }

    //    @Override
    //    public void onWindowFocusChanged(boolean hasWindowFocus) {
    //        super.onWindowFocusChanged(hasWindowFocus);
    //        Log.e("倒计时", "hasWindowFocus:" + hasWindowFocus);
    //        //        if (hasWindowFocus) {
    //        //            loop();
    //        //        } else {
    //        //            stopLoop();
    //        //        }
    //    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoop();
        this.mRunnable = null;
        this.mHandler = null;
    }
}
