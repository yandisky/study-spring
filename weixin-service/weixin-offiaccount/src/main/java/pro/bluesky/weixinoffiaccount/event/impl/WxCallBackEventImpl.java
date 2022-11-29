package pro.bluesky.weixinoffiaccount.event.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;
import pro.bluesky.weixinoffiaccount.event.WxCallBackEvent;

@Component
public class WxCallBackEventImpl implements WxCallBackEvent {
    private static final Logger log = LoggerFactory.getLogger(WxCallBackEventImpl.class);

    @Override
    public void handle(WxConfigDTO wxConfigDTO, JSONObject jsonObject) {
    }
}
