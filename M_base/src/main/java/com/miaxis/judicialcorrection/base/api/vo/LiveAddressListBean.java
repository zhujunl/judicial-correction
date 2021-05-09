package com.miaxis.judicialcorrection.base.api.vo;

import androidx.databinding.BaseObservable;

import java.util.Date;
import java.util.List;

public class LiveAddressListBean {

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

    public static class ListDTO extends BaseObservable{
        public  int  pos;
        public String id;
        public String pid;
        public String pname;
        public String sqsj;
        public String qrdszs;
        public String qrdszsName;
        public String qrdszd;
        public String qrdszdName;
        public String qrdszx;
        public String qrdszxName;
        public String qrdxz;
        public String qrdxzName;
        public String qrdmx;
        public String njsjzdwId;
        public String njsjzdwName;
        public String jzdbgsy;
        public String flowStatusName;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getSqsj() {
            return sqsj;
        }

        public void setSqsj(String sqsj) {
            this.sqsj = sqsj;
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

        public String getQrdszs() {
            return qrdszs;
        }

        public void setQrdszs(String qrdszs) {
            this.qrdszs = qrdszs;
        }

        public String getQrdszsName() {
            return qrdszsName;
        }

        public void setQrdszsName(String qrdszsName) {
            this.qrdszsName = qrdszsName;
        }

        public String getQrdszd() {
            return qrdszd;
        }

        public void setQrdszd(String qrdszd) {
            this.qrdszd = qrdszd;
        }

        public String getQrdszdName() {
            return qrdszdName;
        }

        public void setQrdszdName(String qrdszdName) {
            this.qrdszdName = qrdszdName;
        }

        public String getQrdszx() {
            return qrdszx;
        }

        public void setQrdszx(String qrdszx) {
            this.qrdszx = qrdszx;
        }

        public String getQrdszxName() {
            return qrdszxName;
        }

        public void setQrdszxName(String qrdszxName) {
            this.qrdszxName = qrdszxName;
        }

        public String getQrdxz() {
            return qrdxz;
        }

        public void setQrdxz(String qrdxz) {
            this.qrdxz = qrdxz;
        }

        public String getQrdxzName() {
            return qrdxzName;
        }

        public void setQrdxzName(String qrdxzName) {
            this.qrdxzName = qrdxzName;
        }

        public String getQrdmx() {
            return qrdmx;
        }

        public void setQrdmx(String qrdmx) {
            this.qrdmx = qrdmx;
        }

        public String getNjsjzdwId() {
            return njsjzdwId;
        }

        public void setNjsjzdwId(String njsjzdwId) {
            this.njsjzdwId = njsjzdwId;
        }

        public String getNjsjzdwName() {
            return njsjzdwName;
        }

        public void setNjsjzdwName(String njsjzdwName) {
            this.njsjzdwName = njsjzdwName;
        }

        public String getJzdbgsy() {
            return jzdbgsy;
        }

        public void setJzdbgsy(String jzdbgsy) {
            this.jzdbgsy = jzdbgsy;
        }

        public String getFlowStatusName() {
            return flowStatusName;
        }

        public void setFlowStatusName(String flowStatusName) {
            this.flowStatusName = flowStatusName;
        }
    }
}
