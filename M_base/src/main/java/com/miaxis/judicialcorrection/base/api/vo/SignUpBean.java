package com.miaxis.judicialcorrection.base.api.vo;

import java.util.Date;
import java.util.List;

public class SignUpBean {

    private String total;
    private List<SignUpContentBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<SignUpContentBean> getList() {
        return list;
    }

    public void setList(List<SignUpContentBean> list) {
        this.list = list;
    }
}
