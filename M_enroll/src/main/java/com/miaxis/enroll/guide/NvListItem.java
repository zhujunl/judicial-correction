package com.miaxis.enroll.guide;

import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * NvListItem
 *
 * @author zhangyw
 * Created on 4/30/21.
 */
public class NvListItem {


    public String name;
    public Class<? extends Fragment> targetCls;

    public int index;
    public int max;
    public int current;

    public NvListItem(String name, Class<? extends Fragment> targetCls, int index, int max) {
        this.name = name;
        this.index = index;
        this.max = max;
        this.targetCls = targetCls;
    }

    public boolean isPass() {
        return current > index;
    }

    public boolean isFirst() {
        return index == 0;
    }

    public boolean isLast() {
        return index == max - 1;
    }

    public boolean isChecked() {
        return index == current;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NvListItem that = (NvListItem) o;
        return index == that.index &&
                max == that.max &&
                current == that.current &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index, max, current);
    }

    @Override
    public String toString() {
        return "NvListItem{" +
                "name='" + name + '\'' +
                ", index=" + index +
                ", max=" + max +
                ", current=" + current +
                ", isFirst=" + isFirst() +
                ", isLast=" + isLast() +
                ", isChecked=" + isChecked() +
                ", isPass=" + isPass() +
                '}';
    }
}
