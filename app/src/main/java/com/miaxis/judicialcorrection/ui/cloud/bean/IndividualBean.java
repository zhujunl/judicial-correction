package com.miaxis.judicialcorrection.ui.cloud.bean;


/*{
    "code": 0,
    "desc": "操作成功",
    "data": {
        "id": "a112a540d59c476e9cfdebfdded5b384",
        "pid": "2c92808378a9ec790178aae47e460000",
        "pname": "李*书",
        "jyxxkssj": "2021-03-16T16:00:00.000+0000",
        "jyxxjssj": "2021-03-16T16:03:00.000+0000",
        "jyxxsc": "3",
        "jyxxfs": "3",
        "jyxxfsName": "个别教育",
        "jyxxzynr": "内容啊我是",
        "jlr": "我是记录人"

    }
}
*/
public class IndividualBean {
    private String id;
    private String pid;
    private String pname;
    private String jyxxkssj;
    private String jyxxjssj;
    private String jyxxsc;
    private String jyxxfs;
    private String jyxxfsName;
    private String jyxxzynr;
    private String jlr;

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

    public String getJlr() {
        return jlr;
    }

    public void setJlr(String jlr) {
        this.jlr = jlr;
    }

    @Override
    public String toString() {
        return "IndividualBean{" +
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
                '}';
    }
}
