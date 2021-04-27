package com.miaxis.judicialcorrection.base.databinding;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.DecimalFormat;

import timber.log.Timber;

/**
 * @author yawei
 * @email zyawei@live.com
 */
public class ImageDataBindingAdapter {

    @BindingAdapter("viewBgRes")
    public static void setViewBgRes(View view, int res) {
        view.setBackgroundResource(res);
    }

    @BindingAdapter("imageRes")
    public static void setImageRes(ImageView imageView, int res) {
        Timber.i("url = " + res);
        imageView.setImageResource(res);

    }
    @BindingAdapter("decimalText")
    public static void decimalText(TextView textView, float value) {
        String scoreStr = new DecimalFormat("#.##").format(value);
        textView.setText(scoreStr);
    }


    @BindingAdapter("imageLevel")
    public static void setImageLevel(ImageView imageView, int level) {
        imageView.setImageLevel(level);
    }

}
