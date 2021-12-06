package com.miaxis.enroll.guide.infos;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.miaxis.enroll.EnrollSharedViewModel;

/**
 * Created by junweiliu on 17/1/6.
 */
@Deprecated
public class LimitInputTextWatcher implements TextWatcher {
    /**
     * et
     */
    private EditText et = null;
    /**
     * 挑选条件
     */
    private String regex;
    /**
     * 默许的挑选条件(正则:只能输入中文)
     */
    private String DEFAULT_REGEX = "[^\u4E00-\u9FA5]";

    EnrollSharedViewModel vm;

    /**
     * 构造方法
     *
     * @param et
     */
    public LimitInputTextWatcher(EditText et,  EnrollSharedViewModel vm) {
        this.et = et;
        this.regex = DEFAULT_REGEX;
        this.vm=vm;
    }

    /**
     * 构造方法
     *
     * @param et    et
     * @param regex 挑选条件
     */
    public LimitInputTextWatcher(EditText et, String regex) {
        this.et = et;
        this.regex = regex;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String str = editable.toString();
        String inputStr = clearLimitStr(regex, str);
        et.removeTextChangedListener(this);
        // et.setText方法可能会引发键盘变化,所以用editable.replace来显示内容
        editable.replace(0, editable.length(), inputStr.trim());et.addTextChangedListener(this);
    }

    /**
     * 清除不符合条件的内容
     *
     * @param regex
     * @return
     */
    private String clearLimitStr(String regex, String str) {
        return str.replaceAll(regex, "");
    }
}