package pro.bluesky.weixinoffiaccount.api.exception;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 微信错误码
 */
public class WxError implements Serializable {
    private static final long serialVersionUID = 1706417231058742717L;

    private int errCode;
    private String errMsg;
    private String json;

    public WxError() {
    }

    public WxError(int errCode, String errMsg, String json) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.json = json;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static WxError fromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        WxError wxError = new WxError();
        if (jsonObject.getInteger("errcode") != null) {
            wxError.setErrCode(jsonObject.getInteger("errcode"));
        }
        if (jsonObject.getString("errmsg") != null) {
            wxError.setErrMsg(jsonObject.getString("errmsg"));
        }
        wxError.setJson(jsonObject.toJSONString());

        return wxError;
    }

    @Override
    public String toString() {
        if (this.json == null) {
            return "微信错误code：" + this.errCode + ", 微信错误msg：" + this.errMsg;
        }

        return "微信错误code：" + this.errCode + ", 微信错误msg：" + this.errMsg + "，微信错误json：" + this.json;
    }
}
