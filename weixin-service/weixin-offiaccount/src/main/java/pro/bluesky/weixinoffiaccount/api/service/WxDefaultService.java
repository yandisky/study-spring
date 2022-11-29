package pro.bluesky.weixinoffiaccount.api.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.request.WxGetRequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.request.WxPostRequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.WxResponseHandler;

import java.util.HashMap;

@Service
public class WxDefaultService {
    @Autowired
    private WxRequestService wxRequestService;

    /**
     * 默认微信请求
     *
     * @param wxApiUrl        微信api链接
     * @param wxApiHttpMethod 微信api请求方式
     * @param jsonObject      微信api请求参数
     * @return
     * @throws WxErrorException
     */
    public String httpRequest(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException {
        if (HttpGet.METHOD_NAME.equals(wxApiHttpMethod)) {
            HashMap<String, Object> urlParam = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                urlParam.put(key, jsonObject.get(key));
            }
            return wxRequestService.execute(new WxGetRequestExecutor(), wxApiUrl, urlParam, new WxResponseHandler());
        } else if (HttpPost.METHOD_NAME.equals(wxApiHttpMethod)) {
            return wxRequestService.execute(new WxPostRequestExecutor(), wxApiUrl, jsonObject, new WxResponseHandler());
        } else {
            throw new WxErrorException("微信请求方式错误");
        }
    }
}
