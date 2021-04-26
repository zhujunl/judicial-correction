package com.miaxis.judicialcorrection.base.common;

import android.annotation.SuppressLint;

public class Range {

    private final int startIndex;
    private final int endIndex;

    public Range(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("items=%d-%d", startIndex, endIndex);
    }
}
