package com.miaxis.judicialcorrection.base.api.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Tank
 * @date 2021/5/8 15:42
 * @des
 * @updateAuthor
 * @updateDes
 */

public class Education {

    public Integer total;
    public List<ListBean> list;

    public static class ListBean {
        public String id;
        public Date jyxxkssj;
        public Date jyxxjssj;
        public String jyxxsc;
        public String jyxxfs;
        public String jyxxfsName;
        public String jyxxzynr;
        public String khr;
        public String jzjysj;
        public String jiaoyudidian;
        public String jiaoyuzhuti;
        public String jiaoyuzhutiName;
        public String jiaoyuzhutiDetail;
        public String jiaoyuzhutiDetailName;
        public String zhuchiren;
        public String jiedaoId;
        public String jiedaoName;
        public String personList;

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", jyxxkssj=" + jyxxkssj +
                    ", jyxxjssj=" + jyxxjssj +
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
                    ", jiaoyuzhutiDetailName=" + jiaoyuzhutiDetailName +
                    ", zhuchiren=" + zhuchiren +
                    ", jiedaoId=" + jiedaoId +
                    ", jiedaoName=" + jiedaoName +
                    ", personList=" + personList +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Education{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
