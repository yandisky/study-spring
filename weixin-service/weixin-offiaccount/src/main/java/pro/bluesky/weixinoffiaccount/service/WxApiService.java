package pro.bluesky.weixinoffiaccount.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.service.WxConfigService;
import pro.bluesky.weixinoffiaccount.common.ResponseCodeConst;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;
import pro.bluesky.weixinoffiaccount.event.WxApiEvent;
import pro.bluesky.weixinoffiaccount.event.WxApiEventFactory;

@Service
public class WxApiService {
    @Autowired
    private WxConfigService wxConfigService;
    @Autowired
    private WxApiEventFactory wxApiEventFactory;

    public ResponseDTO config(String postData) {
        WxConfigDTO wxConfigDTO = WxConfigDTO.fromJson(postData);
        wxConfigService.set(wxConfigDTO);
        return ResponseDTO.success();
    }

    public ResponseDTO api(String appId, String postData) {
        JSONObject jsonObject = JSONObject.parseObject(postData);
        String wxApiUrl = jsonObject.getString("wxApiUrl");
        String wxApiHttpMethod = jsonObject.getString("wxApiHttpMethod");
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(wxApiUrl) || StringUtils.isBlank(wxApiHttpMethod)) {
            return ResponseDTO.error(ResponseCodeConst.REQUEST_PARAM_ERROR, jsonObject);
        }
        wxConfigService.setLocalAppId(appId);
        jsonObject.remove("wxApiUrl");
        jsonObject.remove("wxApiHttpMethod");
        try {
            //对微信请求api进行配置转接处理
            WxApiEvent wxApiEvent = wxApiEventFactory.factory(wxApiUrl);
            return wxApiEvent.handle(wxApiUrl, wxApiHttpMethod, jsonObject);
        } catch (WxErrorException e) {
            return ResponseDTO.error(ResponseCodeConst.WEIXIN_ERROR, e.getError(), e.getError().getErrMsg());
        } finally {
            wxConfigService.removeLocalAppId();
        }
    }
}
