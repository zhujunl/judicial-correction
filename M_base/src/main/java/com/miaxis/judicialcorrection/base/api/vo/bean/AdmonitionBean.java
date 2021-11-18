package com.miaxis.judicialcorrection.base.api.vo.bean;


import java.util.Date;
import java.util.List;

/*{
    "code": 0,
    "desc": "操作成功",
    "data": {
        "total": 1,
        "list": [
            {
                "pid": "6447f4064e5a45f896f8dd60b12a8dd6",
                "pname": "夏则华",
                "jgss": "社区矫正对象夏则华在2021年10月19日未经请假，前往象山县处理工作事务。",
                "sfssqsj": "2021-10-19T00:00:00.000+0800"
            }
        ]
    }
}*/
public class AdmonitionBean {
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

    public class Data{
        private String pid;
        private String pname;
        private String jgss;
        private Date sfssqsj;

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

        public String getJgss() {
            return jgss;
        }

        public void setJgss(String jgss) {
            this.jgss = jgss;
        }

        public Date getSfssqsj() {
            return sfssqsj;
        }

        public void setSfssqsj(Date sfssqsj) {
            this.sfssqsj = sfssqsj;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", jgss='" + jgss + '\'' +
                    ", sfssqsj='" + sfssqsj + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AdmonitionBean{" +
                "total='" + total + '\'' +
                ", list=" + list +
                '}';
    }
}
