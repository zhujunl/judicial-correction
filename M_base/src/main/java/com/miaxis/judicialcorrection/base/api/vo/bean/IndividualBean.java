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
                "id": "a112a540d59c476e9cfdebfdded5b384",
                "pid": "2c92808378a9ec790178aae47e460000",
                "pname": "李*书",
//                "jyxxkssj": "2021-03-16T16:00:00.000+0000",
                "jyxxjssj": "2021-03-16T16:03:00.000+0000",
//                "jyxxsc": "3",
                "jyxxfs": "3",
//                "jyxxfsName": "个别教育",
                "jyxxzynr": "内容啊我是",
//                "jlr": "我是记录人"
//            }
        ]
    }
}

*/
public class IndividualBean {
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

    public class Data {
        private String id;
        private String pid;
        private String pname;
        private Date jyxxkssj;
        private String jyxxjssj;
        private String jyxxsc;
        private String jyxxfs;
        private String jyxxfsName;
        private String jyxxzynr;
        private String jlr;
        private String jiaoyudidian;

        public String getJyxxthdd() {
            return jiaoyudidian;
        }

        public void setJyxxthdd(String jyxxthdd) {
            this.jiaoyudidian = jyxxthdd;
        }

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

        public Date getJyxxkssj() {
            return jyxxkssj;
        }

        public void setJyxxkssj(Date jyxxkssj) {
            this.jyxxkssj = jyxxkssj;
        }

        public String getJyxxjssj() {
            return jyxxjssj;
        }

        public void setJyxxjssj(String jyxxjssj) {
            this.jyxxjssj = jyxxjssj;
        }

        public String getJyxxsc() {
            return jyxxsc;
        }

        public void setJyxxsc(String jyxxsc) {
            this.jyxxsc = jyxxsc;
        }

        public String getJyxxfs() {
            return jyxxfs;
        }

        public void setJyxxfs(String jyxxfs) {
            this.jyxxfs = jyxxfs;
        }

        public String getJyxxfsName() {
            return jyxxfsName;
        }

        public void setJyxxfsName(String jyxxfsName) {
            this.jyxxfsName = jyxxfsName;
        }

        public String getJyxxzynr() {
            return jyxxzynr;
        }

        public void setJyxxzynr(String jyxxzynr) {
            this.jyxxzynr = jyxxzynr;
        }

        public String getJlr() {
            return jlr;
        }

        public void setJlr(String jlr) {
            this.jlr = jlr;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", jyxxkssj='" + jyxxkssj + '\'' +
                    ", jyxxjssj='" + jyxxjssj + '\'' +
                    ", jyxxsc='" + jyxxsc + '\'' +
                    ", jyxxfs='" + jyxxfs + '\'' +
                    ", jyxxfsName='" + jyxxfsName + '\'' +
                    ", jyxxzynr='" + jyxxzynr + '\'' +
                    ", jlr='" + jlr + '\'' +
                    ", jyxxthdd='" + jiaoyudidian + '\'' +
                    '}';
        }
    }

}
