package com.miaxis.judicialcorrection.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.miaxis.judicialcorrection.base.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author Tank
 * @date 2021/3/25 14:43
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DatePickDialog extends Dialog implements View.OnClickListener {

    private DatePicker mDp_date;
    private final OnTimeSetListener mOnTimeSetListener;
    private TimePicker mDp_time;

    public DatePickDialog(@NonNull Context context, OnTimeSetListener onTimeSetListener) {
        super(context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_date_pick);
        this.mOnTimeSetListener = onTimeSetListener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Calendar.getInstance().get(Calendar.YEAR);
        this.mDp_date = (DatePicker) findViewById(R.id.dp_date);
        this.mDp_time = (TimePicker) findViewById(R.id.dp_time);
        this.mDp_time.setIs24HourView(true);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        this.mDp_date.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), null);
        resizePicker(this.mDp_date, 120);
        resizePicker(this.mDp_time, 80);
        Date date = new Date();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(date.getTime());
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        mDp_date.setMaxDate(System.currentTimeMillis());
        mDp_time.setHour(hour);
        mDp_time.setMinute(minute);
    }

    @Override
    public void onClick(View v) {
        if (R.id.tv_cancel == v.getId()) {
            dismiss();
        } else {
            if (this.mOnTimeSetListener != null) {
                this.mOnTimeSetListener.onDateSet(this.mDp_date.getYear() + "-" +
                        ((this.mDp_date.getMonth() + 1) < 10 ? ("0" + (this.mDp_date.getMonth() + 1)) : (this.mDp_date.getMonth() + 1)) + "-" +
                        (this.mDp_date.getDayOfMonth() < 10 ? ("0" + this.mDp_date.getDayOfMonth()) : this.mDp_date.getDayOfMonth()) + " " +
                        (this.mDp_time.getHour() < 10 ? ("0" + this.mDp_time.getHour()) : this.mDp_time.getHour()) + ":" +
                        (this.mDp_time.getMinute() < 10 ? ("0" + this.mDp_time.getMinute()) : this.mDp_time.getMinute())
                        + ":00");
                //                this.mOnTimeSetListener.onDateSet(this.mDp_date.getYear() + "-" +
                //                        ((this.mDp_date.getMonth() + 1) < 10 ? ("0" + (this.mDp_date.getMonth() + 1)) : (this.mDp_date.getMonth() + 1)) + "-" +
                //                        (this.mDp_date.getDayOfMonth() < 10 ? ("0" + this.mDp_date.getDayOfMonth()) : this.mDp_date.getDayOfMonth()));
            }
            dismiss();
        }
    }

    private void resizePicker(FrameLayout tp, int width) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np, width);
        }
    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    private void resizeNumberPicker(NumberPicker np, int width) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        np.setLayoutParams(params);
    }

    /**
     * The listener used to indicate the user has finished selecting a date.
     */
    public interface OnTimeSetListener {
        //        /**
        //         * @param year       the selected year
        //         * @param month      the selected month (0-11 for compatibility with
        //         *                   {@link Calendar#MONTH})
        //         * @param dayOfMonth the selected day of the month (1-31, depending on
        //         *                   month)
        //         */
        //        void onDateSet(int year, int month, int dayOfMonth, int hour, int minute);
        void onDateSet(String date);

    }

}
