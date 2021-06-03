package com.miaxis.faceid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.View;

import com.miaxis.judicialcorrection.base.BuildConfig;
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
        if (BuildConfig.EQUIPMENT_TYPE==3){
            mPaint.setStrokeWidth(3F);
        }else {
            mPaint.setStrokeWidth(5F);
        }
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private RectF mRectF;
private boolean mirror=false;
    public void setRect(RectF rect,boolean mirror) {
        this.mRectF = rect;
        this.mirror=mirror;
        //如果出现人脸框与视频中人脸移动方向不对，需进行位置转换，改变mRectF的值
        //float left, float top, float right, float bottom
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mRectF == null) {
            canvas.save();
            canvas.restore();
        } else {
//            Matrix  matrix =new  Matrix();


//            int width=getWidth();
//            int height=getHeight();
//            matrix.setScale(1f, 1f);
//            matrix.postRotate(90);
//            matrix.postScale(width / 2000f, height / 2000f);
//            matrix.postTranslate(width/3f,height/3f);
//           // 偏移量
//           canvas. drawRect(mRectF,this.mPaint);


            int width = getWidth();
            int height = getHeight();
            float left;
            float right;
            float top=mRectF.top;
            float bottom=mRectF.bottom;
            if (mirror){
                 left=  (width- this.mRectF.right);
                 right=  (width- this.mRectF.left);
            }else {
                //无设备暂时无法知道
                if (BuildConfig.EQUIPMENT_TYPE==1){
                    left= this.mRectF.left+(width/10);
                    right= this.mRectF.right+(width/10);
                    top=top+(height/10);
                    bottom=bottom+(height/10);
                }else if (BuildConfig.EQUIPMENT_TYPE==2){
                    //等比0.6
                    left= (float) (this.mRectF.left*0.6);
                    right= (float) (this.mRectF.right*0.6);
                    top= (float) (top*0.6);
                    bottom= (float) (bottom*0.6);
                }else{
                    left= (float) (this.mRectF.left*0.6);
                    right= (float) (this.mRectF.right*0.6);
                    top= (float) (top*0.6);
                    bottom= (float) (bottom*0.6);
                }
            }

//            int top= (int) (height- this.mRectF.top);
//            int bottom= (int) (height- this.mRectF.top);

            canvas.drawRect(left,top,right,bottom, this.mPaint);
        }
    }
}
