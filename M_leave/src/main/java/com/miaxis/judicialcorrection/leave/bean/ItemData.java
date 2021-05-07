package com.miaxis.judicialcorrection.leave.bean;

import java.util.Observable;

/**
 * @author Tank
 * @date 2021/5/7 17:18
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ItemData extends Observable {

    public long id = 0;
    public String name = "name";
    public String location = "location";
    public String startTime = "startTime";
    public String endTime = "endTime";
    public int status = 0;

    public ItemData() {
    }

}
