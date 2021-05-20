package com.miaxis.judicialcorrection.base.api.vo;

import java.util.Date;
import java.util.List;

public class IndividualEducationBean {


    private int total;
    private List<ListDTO> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    public static class ListDTO {
        private String id;
        private String pid;
        private String pname;
        private Date jyxxkssj;
        private Date jyxxjssj;
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

        public Date getJyxxkssj() {
            return jyxxkssj;
        }

        public void setJyxxkssj(Date jyxxkssj) {
            this.jyxxkssj = jyxxkssj;
        }

        public Date getJyxxjssj() {
            return jyxxjssj;
        }

        public void setJyxxjssj(Date jyxxjssj) {
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
    }
}

