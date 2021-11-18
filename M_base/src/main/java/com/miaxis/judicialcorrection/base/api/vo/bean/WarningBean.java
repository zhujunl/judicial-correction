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
                "pid": "db0dea7f6b09425e84f891d41cfc89c3",
                "pname": "俞美群",
                "jgly": "经“雪亮工程”核查发现，社区矫正对象俞美群在接受社区矫正期间，在2021年10月1日无正当理由，未经批准私自外出至东阳市。上述事实有询问笔录、金华市司法局社区矫正精密智控平台精准矫正中拍摄到的照片、东城派出所提供的牌照为浙G039PZ的车辆监控截图、以及社区矫正对象俞美群在2021年10月1日浙江省社区矫正综合管理平台上的定位手机轨迹截图为证。",
                "jgyj": "0203",
                "jgyjName": "(三）违反关于报告、会客、外出、迁居等规定，情节较重的",
                "sfssqsj": "2021-10-21T00:00:00.000+0800"
            }
        ]
    }
}*/
public class WarningBean {
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
        private String jgly;
        private String jgyj;
        private String jgyjName;
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

        public String getJgly() {
            return jgly;
        }

        public void setJgly(String jgly) {
            this.jgly = jgly;
        }

        public String getJgyj() {
            return jgyj;
        }

        public void setJgyj(String jgyj) {
            this.jgyj = jgyj;
        }

        public Date getSfssqsj() {
            return sfssqsj;
        }

        public void setSfssqsj(Date sfssqsj) {
            this.sfssqsj = sfssqsj;
        }


        public String getJgyjName() {
            return jgyjName;
        }

        public void setJgyjName(String jgyjName) {
            this.jgyjName = jgyjName;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", jgly='" + jgly + '\'' +
                    ", jgyj='" + jgyj + '\'' +
                    ", sfssqsj='" + sfssqsj + '\'' +
                    ", jgyjName='" + jgyjName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WarningBean{" +
                "total='" + total + '\'' +
                ", list=" + list +
                '}';
    }
}
