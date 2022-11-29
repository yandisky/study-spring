package pro.bluesky.weixinoffiaccount.event;

import com.alibaba.fastjson.JSONObject;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;

public interface WxApiEvent {
    ResponseDTO handle(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException;
}
