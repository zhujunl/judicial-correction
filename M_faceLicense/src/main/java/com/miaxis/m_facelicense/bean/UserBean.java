package com.miaxis.m_facelicense.bean;

/**
 * @author ZJL
 * @date 2022/10/17 15:30
 * @des
 * @updateAuthor
 * @updateDes
 */
public class UserBean {
    public String account;
    public String password;

    public UserBean() {
    }

    public UserBean(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
