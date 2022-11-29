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
public class WxApiEventMediaGetImpl implements WxApiEvent {
    @Autowired
    private WxMediaService wxMediaService;

    @Override
    public ResponseDTO handle(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException {
        String mediaId = jsonObject.getString("media_id");
        if (StringUtils.isBlank(mediaId)) {
            return ResponseDTO.error(ResponseCodeConst.ERROR_PARAM, "参数media_id错误");
        }
        String response = wxMediaService.getMedia(wxApiUrl, mediaId);
        return ResponseDTO.success(JSONObject.parseObject(response));
    }
}
