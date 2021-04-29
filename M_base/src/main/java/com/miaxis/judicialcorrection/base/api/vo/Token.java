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
}
