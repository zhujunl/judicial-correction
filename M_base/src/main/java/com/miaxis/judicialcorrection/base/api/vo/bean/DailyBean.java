package com.miaxis.judicialcorrection.base.api.vo.bean;

/*
{
        "code": 0,
        "desc": "操作成功",
        "data": {
        "total": 2,
        "list": [
        {
        "id": "005a1ce349db4eec9cfcf6ea6e5f3481",
        "pid": "2c928083786d7e8801787a8d9ffb001a",
        "pname": "李**",
        "bgsj": "2021-04-01T07:23:45.000+0000",
        "bgfs": "1",
        "bgfsName": "指纹报告",
        "bgnr": "我是报告内容啊啊\r\n",
        "jlr": "卢*",
        "jlsj": "2021-04-01T07:23:45.000+0000"
        },
        {
        "id": "82f109d4433c4045b84468789f5a69d0",
        "pid": "2c928083786d7e8801787a8d9ffb001a",
        "pname": "李**",
        "bgsj": "2021-04-01T07:20:57.000+0000",
        "bgfs": "2",
        "bgfsName": "人脸报告",
        "bgnr": "112",
        "jlr": "卢*",
        "jlsj": "2021-04-01T07:20:57.000+0000"
        }
        ]
        }
        }*/

import androidx.databinding.BaseObservable;

import java.util.Date;
import java.util.List;

public class DailyBean {
    private String total;
    private List<Data> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    /* "id": "005a1ce349db4eec9cfcf6ea6e5f3481",
            "pid": "2c928083786d7e8801787a8d9ffb001a",
            "pname": "李**",
            "bgsj": "2021-04-01T07:23:45.000+0000",
            "bgfs": "1",
            "bgfsName": "指纹报告",
            "bgnr": "我是报告内容啊啊\r\n",
            "jlr": "卢*",
            "jlsj": "2021-04-01T07:23:45.000+0000"*/
    public static class Data extends BaseObservable{
        private String id;
        private String pid;
        private String pname;
        private Date bgsj;
        private String bgxsName;
        private String bgnr;
        private String jlr;
        private String jlsj;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public Date getBgsj() {
            return bgsj;
        }

        public void setBgsj(Date bgsj) {
            this.bgsj = bgsj;
        }

        public String getBgfsName() {
            return bgxsName;
        }

        public void setBgfsName(String bgfsName) {
            this.bgxsName = bgfsName;
        }

        public String getBgnr() {
            return bgnr;
        }

        public void setBgnr(String bgnr) {
            this.bgnr = bgnr;
        }

        public String getJlr() {
            return jlr;
        }

        public void setJlr(String jlr) {
            this.jlr = jlr;
        }

        public String getJlsj() {
            return jlsj;
        }

        public void setJlsj(String jlsj) {
            this.jlsj = jlsj;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", bgsj='" + bgsj + '\'' +
                    ", bgfsName='" + bgxsName + '\'' +
                    ", bgnr='" + bgnr + '\'' +
                    ", jlr='" + jlr + '\'' +
                    ", jlsj='" + jlsj + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DailyBean{" +
                "total='" + total + '\'' +
                ", list=" + list +
                '}';
    }
}
