package pro.bluesky.weixinoffiaccount.event.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.service.WxDefaultService;
import pro.bluesky.weixinoffiaccount.api.service.WxQrcodeService;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;
import pro.bluesky.weixinoffiaccount.event.WxApiEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WxApiEventQrcodeImpl implements WxApiEvent {
    @Autowired
    private WxQrcodeService wxQrcodeService;
    @Autowired
    private WxDefaultService wxDefaultService;

    @Override
    public ResponseDTO handle(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException {
        String response;
        String sceneStr = jsonObject.getString("uuid");
        Integer expireSeconds = jsonObject.getInteger("expireSeconds");
        //判断是否存在特定参数，不存在则按照微信api参数规则请求
        if (StringUtils.isBlank(sceneStr) || expireSeconds == null) {
            response = wxDefaultService.httpRequest(wxApiUrl, wxApiHttpMethod, jsonObject);
        } else {
            if (expireSeconds <= 0) {
                response = wxQrcodeService.createPermanentQrcode(wxApiUrl, sceneStr);
            } else {
                response = wxQrcodeService.createTemporaryQrcode(wxApiUrl, sceneStr, expireSeconds);
            }
        }
        JSONObject responseJsonObject = JSONObject.parseObject(response);
        if (responseJsonObject != null) {
            Long responseExpireSeconds = responseJsonObject.getLong("expire_seconds");
            if (responseExpireSeconds != null) {
                LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(responseExpireSeconds);
                responseJsonObject.put("expiry_date",
                        localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                responseJsonObject.put("expire_seconds", 0);
                responseJsonObject.put("expiry_date", "");
            }
        }
        return ResponseDTO.success(responseJsonObject);
    }
}
