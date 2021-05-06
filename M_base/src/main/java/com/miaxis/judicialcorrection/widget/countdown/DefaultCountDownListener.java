package com.miaxis.judicialcorrection.widget.countdown;

/**
 * DefaultCountDownListener
 *
 * @author zhangyw
 * Created on 4/30/21.
 */
public abstract class DefaultCountDownListener implements CountDownListener {


    @Override
    public void onCountDownStart() {

    }

    @Override
    public void onCountDownProgress(int progress) {

    }

    @Override
    public abstract void onCountDownDone();
}
