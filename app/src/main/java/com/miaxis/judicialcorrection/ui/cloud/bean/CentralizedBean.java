package com.miaxis.judicialcorrection.ui.cloud.bean;


/*{
    "code": 0,
    "desc": "操作成功",
    "data": {
        "id": "ce7cafea110b4d078d89b630c7774ee2",
        "jzjyId": "c20177ba526241cca0546bb40d0097a7",
        "pid": "8a82e6d178a6daeb0178a6e8146e0000",
        "pname": "接口添加测试1",
        "pingjia": "好aa",
        "qiandaoshijian": "2021-04-17T06:44:48.887+0000",
        "qiantuishijian": null,
        "educationVo": null
    }
}
*/
public class CentralizedBean {
    private String id;
    private String jzjyId;
    private String pid;
    private String pname;
    private String pingjia;
    private String qiandaoshijian;
    private String qiantuishijian;
    private String educationVo;

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

    public String getEducationVo() {
        return educationVo;
    }

    public void setEducationVo(String educationVo) {
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
                ", educationVo='" + educationVo + '\'' +
                '}';
    }
}
