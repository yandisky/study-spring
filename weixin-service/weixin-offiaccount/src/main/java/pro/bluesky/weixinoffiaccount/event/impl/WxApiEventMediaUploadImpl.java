package pro.bluesky.weixinoffiaccount.event.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.service.WxMediaService;
import pro.bluesky.weixinoffiaccount.common.ResponseCodeConst;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;
import pro.bluesky.weixinoffiaccount.event.WxApiEvent;

@Component
public class WxApiEventMediaUploadImpl implements WxApiEvent {
    @Autowired
    private WxMediaService wxMediaService;

    @Override
    public ResponseDTO handle(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException {
        String type = jsonObject.getString("type");
        String fileUrl = jsonObject.getString("fileUrl");
        if (StringUtils.isBlank(type) || StringUtils.isBlank(fileUrl)) {
            return ResponseDTO.error(ResponseCodeConst.ERROR_PARAM, "参数type,fileUrl错误");
        }
        String response = wxMediaService.uploadMedia(wxApiUrl, type, fileUrl);
        return ResponseDTO.success(JSONObject.parseObject(response));
    }
}
