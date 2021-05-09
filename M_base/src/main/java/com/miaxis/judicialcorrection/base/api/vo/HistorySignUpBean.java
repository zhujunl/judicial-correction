package com.miaxis.judicialcorrection.base.api.vo;

import java.util.List;

public class HistorySignUpBean {
//    唯一标识	id	string
//    社区服务开始时间	sqfwkssj	date
//    社区服务结束时间	sqfwjssj	date
//    社区服务时长	sqfwsc	string
//    社区服务地点	sqfwdd	string
//    社区服务内容	sqfwnr	string
//    记录人	jlr	string
//    矫正机构	jiedaoId	string
//    矫正机构名称	jiedaoName	string
//    参与矫正对象列表	personList	list


    public String total;

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
