package com.miaxis.judicialcorrection.base.api;
import com.google.gson.annotations.SerializedName;

public class TokenResult<T>{
    @SerializedName(value = "access_token")
    public String  access_token;
    @SerializedName(value = "scope")
    public String scope;
    @SerializedName(value = "token_type")
    private String token_type;
    @SerializedName(value = "expires_time")
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

    public String getExpires_time() {
        return expires_time;
    }

    public void setExpires_time(String expires_time) {
        this.expires_time = expires_time;
    }

    @Override
    public String toString() {
        return "TokenResult{" +
                "access_token=" + access_token +
                ", scope='" + scope + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_time='" + expires_time + '\'' +
                '}';
    }
}
