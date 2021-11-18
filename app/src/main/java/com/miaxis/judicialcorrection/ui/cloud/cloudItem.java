package com.miaxis.judicialcorrection.ui.cloud;

import androidx.fragment.app.Fragment;

public class cloudItem {
    private String name;
    private Class<? extends Fragment> fargment;
    private Boolean Ischeck;

    public cloudItem(String name, Class<? extends Fragment> fargment,Boolean ischeck) {
        this.name = name;
        this.fargment = fargment;
        this.Ischeck=ischeck;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends Fragment> getFargment() {
        return fargment;
    }

    public void setFargment(Class<? extends Fragment> fargment) {
        this.fargment = fargment;
    }

    public Boolean getIscheck() {
        return Ischeck;
    }

    public void setIscheck(Boolean ischeck) {
        Ischeck = ischeck;
    }

    @Override
    public String toString() {
        return "cloudItem{" +
                "name='" + name + '\'' +
                ", fargment=" + fargment +
                ", Ischeck=" + Ischeck +
                '}';
    }
}
