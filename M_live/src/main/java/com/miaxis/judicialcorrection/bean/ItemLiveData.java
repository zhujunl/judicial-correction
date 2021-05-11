package com.miaxis.judicialcorrection.bean;

import androidx.databinding.BaseObservable;

import java.util.Observable;

/**
 * @author Tank
 * @date 2021/5/7 17:18
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ItemLiveData extends BaseObservable {

    public long id = 0;
    public String name = "name";
    public String unit="unit";
    public String location = "location";
    public String time = "time";

    public ItemLiveData() {
    }

}
