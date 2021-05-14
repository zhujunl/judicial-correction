package com.miaxis.judicialcorrection.base.api.vo;

/**
 * Token
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
public class Token {
    public String access_token;
    public long expires_time;


    public boolean isExpires() {
        return System.currentTimeMillis() > expires_time;
    }

    public String getBearerToken() {
        return String.format("Bearer %s", access_token);
    }

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                "isExpires='" + isExpires() + '\'' +
                ", expires_time=" + expires_time +
                '}';
    }
}
