package com.miaxis.judicialcorrection.base.api.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Tank
 * @date 2021/5/8 17:03
 * @des
 * @updateAuthor
 * @updateDes
 */

public class Leave {

    public Integer total;
    public List<ListBean> list;

    public static class ListBean {

        public int index;
        public String id;
        public String pid;
        public String pname;
        public String sqsj;
        public String wcly;
        public String wclyName;
        public String wcts;
        public Date ksqr;
        public Date jsrq;
        public String sfyxj;
        public String sfyxjName;
        public String xjsj;
        public String xjms;
        public String flowStatusName;
        public String lsjhr;
        public String gx;
        public String lxdh;
        public String wcmdd;
        public String jtsy;
        public List<ListBean.ListItem> list;

        public static class ListItem {
            public String pid;
            public String wcmddszs;
            public String wcmddszsName;
            public String wcmddszd;
            public String wcmddszdName;
            public String wcmddszx;
            public String wcmddszxName;
            public String wcmddxz;
            public String wcmddxzName;
            public String wcmddmx;

            @Override
            public String toString() {
                return "ListItem{" +
                        "pid='" + pid + '\'' +
                        ", wcmddszs='" + wcmddszs + '\'' +
                        ", wcmddszsName='" + wcmddszsName + '\'' +
                        ", wcmddszd='" + wcmddszd + '\'' +
                        ", wcmddszdName='" + wcmddszdName + '\'' +
                        ", wcmddszx='" + wcmddszx + '\'' +
                        ", wcmddszxName='" + wcmddszxName + '\'' +
                        ", wcmddxz='" + wcmddxz + '\'' +
                        ", wcmddxzName='" + wcmddxzName + '\'' +
                        ", wcmddmx='" + wcmddmx + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", sqsj='" + sqsj + '\'' +
                    ", wcly='" + wcly + '\'' +
                    ", wclyName='" + wclyName + '\'' +
                    ", wcts='" + wcts + '\'' +
                    ", ksqr='" + ksqr + '\'' +
                    ", jsrq='" + jsrq + '\'' +
                    ", sfyxj=" + sfyxj +
                    ", sfyxjName=" + sfyxjName +
                    ", xjsj=" + xjsj +
                    ", xjms=" + xjms +
                    ", flowStatusName='" + flowStatusName + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Leave{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
