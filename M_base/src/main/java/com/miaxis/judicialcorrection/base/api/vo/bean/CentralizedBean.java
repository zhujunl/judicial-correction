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
                "id": "0021E12EB12D46439B7D58985D8C0D67",
                "jzjyId": "0395753144a2423b9e937400710f6258",
                "pid": "3d3d41824115461db03e92faf6e33cbf",
                "pname": "吕*杰",
                "pingjia": "较好",
                "qiandaoshijian": null,
                "qiantuishijian": null,
                "educationVo": {
                    "id": "0395753144a2423b9e937400710f6258",
                    "jyxxkssj": null,
                    "jyxxjssj": null,
                    "jyxxsc": "0.66",
                    "jyxxfs": "1",
                    "jyxxfsName": "集中教育",
                    "jyxxzynr": "法律案例分析",
                    "khr": "石**",
                    "jzjysj": "2019-11-13T16:00:00.000+0000",
                    "jiaoyudidian": "海门社区矫正执法中队",
                    "jiaoyuzhuti": null,
                    "jiaoyuzhutiName": null,
                    "jiaoyuzhutiDetail": null,
                    "jiaoyuzhutiDetailName": null,
                    "zhuchiren": null,
                    "jiedaoId": "DEPT0000000000000000000000903341",
                    "jiedaoName": "社区矫正执法大队海门中队",
                    "personList": null
                }
            }

        ]
    }
}
*/
public class CentralizedBean {
    private String total;
    private List<li> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<li> getList() {
        return list;
    }

    public void setList(List<li> list) {
        this.list = list;
    }

    public class li{
        private String id;
        private String jzjyId;
        private String pid;
        private String pname;
        private String pingjia;
        private String qiandaoshijian;
        private String qiantuishijian;
        private Data educationVo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJzjyId() {
            return jzjyId;
        }

        public void setJzjyId(String jzjyId) {
            this.jzjyId = jzjyId;
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

        public String getPingjia() {
            return pingjia;
        }

        public void setPingjia(String pingjia) {
            this.pingjia = pingjia;
        }

        public String getQiandaoshijian() {
            return qiandaoshijian;
        }

        public void setQiandaoshijian(String qiandaoshijian) {
            this.qiandaoshijian = qiandaoshijian;
        }

        public String getQiantuishijian() {
            return qiantuishijian;
        }

        public void setQiantuishijian(String qiantuishijian) {
            this.qiantuishijian = qiantuishijian;
        }

        public Data getEducationVo() {
            return educationVo;
        }

        public void setEducationVo(Data educationVo) {
            this.educationVo = educationVo;
        }

        @Override
        public String toString() {
            return "CentralizedBean{" +
                    "id='" + id + '\'' +
                    ", jzjyId='" + jzjyId + '\'' +
                    ", pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", pingjia='" + pingjia + '\'' +
                    ", qiandaoshijian='" + qiandaoshijian + '\'' +
                    ", qiantuishijian='" + qiantuishijian + '\'' +
                    ", educationVo=" + educationVo +
                    '}';
        }

        public class Data {
            private String id;
            private String jyxxkssj;
            private String jyxxjssj;
            private String jyxxsc;
            private String jyxxfs;
            private String jyxxfsName;
            private String jyxxzynr;
            private String khr;
            private Date jzjysj;
            private String jiaoyudidian;
            private String jiaoyuzhuti;
            private String jiaoyuzhutiName;
            private String jiaoyuzhutiDetail;
            private String jiaoyuzhutiDetailName;
            private String zhuchiren;
            private String jiedaoId;
            private String jiedaoName;
            private String personList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getJyxxkssj() {
                return jyxxkssj;
            }

            public void setJyxxkssj(String jyxxkssj) {
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

            public String getKhr() {
                return khr;
            }

            public void setKhr(String khr) {
                this.khr = khr;
            }

            public Date getJzjysj() {
                return jzjysj;
            }

            public void setJzjysj(Date jzjysj) {
                this.jzjysj = jzjysj;
            }

            public String getJiaoyudidian() {
                return jiaoyudidian;
            }

            public void setJiaoyudidian(String jiaoyudidian) {
                this.jiaoyudidian = jiaoyudidian;
            }

            public String getJiaoyuzhuti() {
                return jiaoyuzhuti;
            }

            public void setJiaoyuzhuti(String jiaoyuzhuti) {
                this.jiaoyuzhuti = jiaoyuzhuti;
            }

            public String getJiaoyuzhutiName() {
                return jiaoyuzhutiName;
            }

            public void setJiaoyuzhutiName(String jiaoyuzhutiName) {
                this.jiaoyuzhutiName = jiaoyuzhutiName;
            }

            public String getJiaoyuzhutiDetail() {
                return jiaoyuzhutiDetail;
            }

            public void setJiaoyuzhutiDetail(String jiaoyuzhutiDetail) {
                this.jiaoyuzhutiDetail = jiaoyuzhutiDetail;
            }

            public String getJiaoyuzhutiDetailName() {
                return jiaoyuzhutiDetailName;
            }

            public void setJiaoyuzhutiDetailName(String jiaoyuzhutiDetailName) {
                this.jiaoyuzhutiDetailName = jiaoyuzhutiDetailName;
            }

            public String getZhuchiren() {
                return zhuchiren;
            }

            public void setZhuchiren(String zhuchiren) {
                this.zhuchiren = zhuchiren;
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

            @Override
            public String toString() {
                return "Data{" +
                        "id='" + id + '\'' +
                        ", jyxxkssj='" + jyxxkssj + '\'' +
                        ", jyxxjssj='" + jyxxjssj + '\'' +
                        ", jyxxsc='" + jyxxsc + '\'' +
                        ", jyxxfs='" + jyxxfs + '\'' +
                        ", jyxxfsName='" + jyxxfsName + '\'' +
                        ", jyxxzynr='" + jyxxzynr + '\'' +
                        ", khr='" + khr + '\'' +
                        ", jzjysj='" + jzjysj + '\'' +
                        ", jiaoyudidian='" + jiaoyudidian + '\'' +
                        ", jiaoyuzhuti='" + jiaoyuzhuti + '\'' +
                        ", jiaoyuzhutiName='" + jiaoyuzhutiName + '\'' +
                        ", jiaoyuzhutiDetail='" + jiaoyuzhutiDetail + '\'' +
                        ", jiaoyuzhutiDetailName='" + jiaoyuzhutiDetailName + '\'' +
                        ", zhuchiren='" + zhuchiren + '\'' +
                        ", jiedaoId='" + jiedaoId + '\'' +
                        ", jiedaoName='" + jiedaoName + '\'' +
                        ", personList='" + personList + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "CentralizedBean{" +
                "total='" + total + '\'' +
                ", list=" + list +
                '}';
    }
}
