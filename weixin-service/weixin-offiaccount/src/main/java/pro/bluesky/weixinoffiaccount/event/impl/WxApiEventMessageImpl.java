package pro.bluesky.weixinoffiaccount.event.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.service.WxDefaultService;
import pro.bluesky.weixinoffiaccount.api.service.WxMediaService;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;
import pro.bluesky.weixinoffiaccount.event.WxApiEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class WxApiEventMessageImpl implements WxApiEvent {
    @Autowired
    private WxMediaService wxMediaService;
    @Autowired
    private WxDefaultService wxDefaultService;
    @Autowired
    private RedissonClient redissonClient;

    @Value("${weixin.redissonKeyPrefix}")
    private String redissonKeyPrefix;
    @Value("${weixin.offiaccount.mediaUploadUrl}")
    private String mediaUploadUrl;

    @Override
    public ResponseDTO handle(String wxApiUrl, String wxApiHttpMethod, JSONObject jsonObject) throws WxErrorException {
        String msgType = jsonObject.getString("msgtype");
        if ("news".equals(msgType)) {
            //转换articles的格式
            JSONObject news = jsonObject.getJSONObject("news");
            JSONObject articles = news.getJSONObject("articles");
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(articles);
            news.put("articles", jsonArray);
            jsonObject.put("news", news);
        } else if ("image".equals(msgType) || "voice".equals(msgType) || "video".equals(msgType)) {
            String fileUrl = jsonObject.getString("fileUrl");
            //当存在fileUrl参数时，才触发上传素材并且发送素材消息
            if (!StringUtils.isBlank(fileUrl)) {
                String response = getCacheMedia(fileUrl);
                if (StringUtils.isBlank(response)) {
                    response = wxMediaService.uploadMedia(mediaUploadUrl, msgType, fileUrl);
                    setCacheMedia(fileUrl, response);
                }
                jsonObject.remove("fileUrl");
                jsonObject.put(msgType, JSONObject.parseObject(response));
            }
        }
        String response = wxDefaultService.httpRequest(wxApiUrl, wxApiHttpMethod, jsonObject);
        return ResponseDTO.success(JSONObject.parseObject(response));
    }

    private String getCacheMedia(String fileUrl) {
        String fileUnique = UUID.nameUUIDFromBytes((fileUrl).getBytes()).toString().replaceAll("-", "");
        RBucket<String> redissonClientBucket = redissonClient.getBucket(redissonKeyPrefix + "media_" + fileUnique);
        if (!redissonClientBucket.isExists()) {
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", redissonClientBucket.get());
        return jsonObject.toJSONString();
    }

    private void setCacheMedia(String fileUrl, String response) {
        String fileUnique = UUID.nameUUIDFromBytes((fileUrl).getBytes()).toString().replaceAll("-", "");
        RBucket<String> redissonClientBucket = redissonClient.getBucket(redissonKeyPrefix + "media_" + fileUnique);
        JSONObject jsonObject = JSONObject.parseObject(response);
        redissonClientBucket.set(jsonObject.getString("media_id"), 172800L, TimeUnit.SECONDS);
    }
}
