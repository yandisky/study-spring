package pro.bluesky.weixinoffiaccount.event;

import com.alibaba.fastjson.JSONObject;
import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;

public interface WxCallBackEvent {
    void handle(WxConfigDTO wxConfigDTO, JSONObject jsonObject);
}
