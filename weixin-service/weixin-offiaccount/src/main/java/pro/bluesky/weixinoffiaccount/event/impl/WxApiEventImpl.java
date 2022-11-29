package pro.bluesky.weixinoffiaccount.event.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.service.WxDefaultService;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;
import pro.bluesky.weixinoffiaccount.event.WxApiEvent;

@Component
public class WxApiEventImpl implements WxApiEvent {
    @Autowired
    private WxDefaultService wxDefaultService;

    @Override
    public ResponseDTO handle(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException {
        String response = wxDefaultService.httpRequest(wxApiUrl, wxApiHttpMethod, jsonObject);
        return ResponseDTO.success(JSONObject.parseObject(response));
    }
}
