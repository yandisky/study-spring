package pro.bluesky.weixinoffiaccount.api.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.request.WxPostRequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.WxResponseHandler;

@Service
public class WxQrcodeService {
    @Autowired
    private WxRequestService wxRequestService;

    /**
     * 创建临时带参数二维码
     *
     * @param wxApiUrl      微信api链接
     * @param sceneStr      二维码携带参数
     * @param expireSeconds 二维码过期时间
     * @return
     * @throws WxErrorException
     */
    public String createTemporaryQrcode(String wxApiUrl, String sceneStr, Integer expireSeconds) throws WxErrorException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_name", "QR_STR_SCENE");
        jsonObject.put("expire_seconds", expireSeconds);
        JSONObject actionInfo = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str", sceneStr);
        actionInfo.put("scene", scene);
        jsonObject.put("action_info", actionInfo);
        return wxRequestService.execute(new WxPostRequestExecutor(), wxApiUrl, jsonObject, new WxResponseHandler());
    }

    /**
     * 创建永久带参数二维码
     *
     * @param wxApiUrl 微信api链接
     * @param sceneStr 二维码携带参数
     * @return
     * @throws WxErrorException
     */
    public String createPermanentQrcode(String wxApiUrl, String sceneStr) throws WxErrorException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_name", "QR_LIMIT_STR_SCENE");
        JSONObject actionInfo = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str", sceneStr);
        actionInfo.put("scene", scene);
        jsonObject.put("action_info", actionInfo);
        return wxRequestService.execute(new WxPostRequestExecutor(), wxApiUrl, jsonObject, new WxResponseHandler());
    }
}
