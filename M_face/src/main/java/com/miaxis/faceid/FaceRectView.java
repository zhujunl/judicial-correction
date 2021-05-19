package com.miaxis.faceid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.miaxis.judicialcorrection.face.R;

import androidx.annotation.Nullable;

/**
 * @author Tank
 * @date 2021/5/19 9:04
 * @des
 * @updateAuthor
 * @updateDes
 */
public class FaceRectView extends View {

    private final Paint mPaint = new Paint();

    public FaceRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(getResources().getColor(R.color.color_face_rect));
        mPaint.setStrokeWidth(5F);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private RectF mRectF;

    public void setRect(RectF rect) {
        this.mRectF = rect;
        //如果出现人脸框与视频中人脸移动方向不对，需进行位置转换，改变mRectF的值
        //float left, float top, float right, float bottom
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mRectF == null) {
            canvas.restore();
        } else {
            canvas.drawRect(this.mRectF, this.mPaint);
        }
    }
}
