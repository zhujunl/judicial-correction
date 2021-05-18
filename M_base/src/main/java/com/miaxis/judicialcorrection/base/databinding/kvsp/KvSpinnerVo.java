package com.miaxis.judicialcorrection.base.databinding.kvsp;

import androidx.databinding.BaseObservable;

/**
 * SpVo
 *
 * @author zhangyw
 * Created on 5/17/21.
 */
public  class KvSpinnerVo extends BaseObservable {
    public String key;
    public String value;

    public KvSpinnerVo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KvSpinnerVo() {
    }

    @Override
    public String toString() {
        return "SpVo{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}