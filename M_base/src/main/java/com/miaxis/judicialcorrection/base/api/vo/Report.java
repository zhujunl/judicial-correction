package com.miaxis.judicialcorrection.base.api.vo;

import java.util.List;

/**
 * @author Tank
 * @date 2021/5/8 15:04
 * @des
 * @updateAuthor
 * @updateDes
 */
public class Report {

    public Integer total;
    public List<ListBean> list;

    public static class ListBean {
        public String id;
        public String pid;
        public String pname;
        public String bgsj;
        public String bgfs;
        public String bgfsName;
        public String bgnr;
        public String jlr;
        public String jlsj;

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", pid='" + pid + '\'' +
                    ", pname='" + pname + '\'' +
                    ", bgsj='" + bgsj + '\'' +
                    ", bgfs='" + bgfs + '\'' +
                    ", bgfsName='" + bgfsName + '\'' +
                    ", bgnr='" + bgnr + '\'' +
                    ", jlr='" + jlr + '\'' +
                    ", jlsj='" + jlsj + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Report{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
