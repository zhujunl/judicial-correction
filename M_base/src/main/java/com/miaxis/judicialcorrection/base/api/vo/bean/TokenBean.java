package com.miaxis.judicialcorrection.base.api.vo.bean;


/*{
    "access_token": "9a09ecb5-77ad-4b1d-ad3d-a165a61761c7",
    "scope": "read write",
    "token_type": "bearer",
    "expires_in": 2839,
    "expires_time": 1637144159866
}*/
public class TokenBean {
    private String access_token;
    private String scope;
    private String token_type;
    private String expires_in;
    private String expires_time;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getExpires_time() {
        return expires_time;
    }

    public void setExpires_time(String expires_time) {
        this.expires_time = expires_time;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "access_token='" + access_token + '\'' +
                ", scope='" + scope + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", expires_time='" + expires_time + '\'' +
                '}';
    }
}
