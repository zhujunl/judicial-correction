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
                "gyldId": "ed706f7bea9f4c6bb10fdb28b1f1f775",
                "pid": "2c928083786d7e8801787a8d9ffb001a",
                "pname": "李*书",
                "sqfwbx": "表现很好哦",
                "publicActivityVo": {
                    "id": "ed706f7bea9f4c6bb10fdb28b1f1f775",
                    "sqfwkssj": "2021-03-31T08:06:30.000+0000",
                    "sqfwjssj": "2021-03-31T08:06:33.000+0000",
                    "sqfwsc": "1",
                    "sqfwdd": "11",
                    "sqfwnr": "23",
                    "jlr": "444",
                    "jiedaoId": "DEPT0000000000000000000000903341",
                    "jiedaoName": "社区矫正执法大队海门中队",
                    "personList": null
                }
            }
        ]
    }
}
*/
public class WelfareBean {
    private String total;
    private List<Dat> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Dat> getList() {
        return list;
    }

    public void setList(List<Dat> list) {
        this.list = list;
    }

    public class Dat{
        private String gyldId;
        private String pid;
        private String pname;
        private String sqfwbx;
        private PublicActivityVo publicActivityVo;

        public String getGyldId() {
            return gyldId;
        }

        public void setGyldId(String gyldId) {
            this.gyldId = gyldId;
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

        public String getSqfwbx() {
            return sqfwbx;
        }

        public void setSqfwbx(String sqfwbx) {
            this.sqfwbx = sqfwbx;
        }

        public PublicActivityVo getPublicActivityVo() {
            return publicActivityVo;
        }

        public void setPublicActivityVo(PublicActivityVo publicActivityVo) {
            this.publicActivityVo = publicActivityVo;
        }

        public class PublicActivityVo{
            private String id;
            private Date sqfwkssj;
            private String sqfwjssj;
            private String sqfwsc;
            private String sqfwdd;
            private String sqfwnr;
            private String jlr;
            private String jiedaoId;
            private String jiedaoName;
            private String personList;
            private String gyldxsName;

            public String getSqffxs() {
                return gyldxsName;
            }

            public void setSqffxs(String sqffxs) {
                this.gyldxsName = sqffxs;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Date getSqfwkssj() {
                return sqfwkssj;
            }

            public void setSqfwkssj(Date sqfwkssj) {
                this.sqfwkssj = sqfwkssj;
            }

            public String getSqfwjssj() {
                return sqfwjssj;
            }

            public void setSqfwjssj(String sqfwjssj) {
                this.sqfwjssj = sqfwjssj;
            }

            public String getSqfwsc() {
                return sqfwsc;
            }

            public void setSqfwsc(String sqfwsc) {
                this.sqfwsc = sqfwsc;
            }

            public String getSqfwdd() {
                return sqfwdd;
            }

            public void setSqfwdd(String sqfwdd) {
                this.sqfwdd = sqfwdd;
            }

            public String getSqfwnr() {
                return sqfwnr;
            }

            public void setSqfwnr(String sqfwnr) {
                this.sqfwnr = sqfwnr;
            }

            public String getJlr() {
                return jlr;
            }

            public void setJlr(String jlr) {
                this.jlr = jlr;
            }

            public String getJiedaoId() {
                return jiedaoId;
            }

            public void setJiedaoId(String jiedaoId) {
                this.jiedaoId = jiedaoId;
            }

            public String getJiedaoName() {
                return jiedaoName;
            }

            public void setJiedaoName(String jiedaoName) {
                this.jiedaoName = jiedaoName;
            }

            public String getPersonList() {
                return personList;
            }

            public void setPersonList(String personList) {
                this.personList = personList;
            }
        }
    }
}
