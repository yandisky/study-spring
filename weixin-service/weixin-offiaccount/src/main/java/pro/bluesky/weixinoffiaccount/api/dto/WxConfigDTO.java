package pro.bluesky.weixinoffiaccount.api.dto;

import com.alibaba.fastjson.JSONObject;

public class WxConfigDTO {
    private String appId;
    private String appSecret;
    private String token;
    private String encodingAESKey;
    private String getTokenUrl;

    public static WxConfigDTO fromJson(String json) {
        return JSONObject.parseObject(json, WxConfigDTO.class);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    public String getGetTokenUrl() {
        return getTokenUrl;
    }

    public void setGetTokenUrl(String getTokenUrl) {
        this.getTokenUrl = getTokenUrl;
    }
}
