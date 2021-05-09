package com.miaxis.judicialcorrection.base.api.vo;

import java.util.Date;
import java.util.List;

public class SignUpBean {

    private String total;
    private List<ListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String gyldId;
        private String pid;
        private String pname;
        private String sqfwbx;
        private SignUpContentBean publicActivityVo;

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

        public SignUpContentBean getPublicActivityVo() {
            return publicActivityVo;
        }

        public void setPublicActivityVo(SignUpContentBean publicActivityVo) {
            this.publicActivityVo = publicActivityVo;
        }
    }


}
