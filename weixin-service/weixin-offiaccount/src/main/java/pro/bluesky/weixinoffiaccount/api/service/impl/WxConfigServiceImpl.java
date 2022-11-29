package pro.bluesky.weixinoffiaccount.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;
import pro.bluesky.weixinoffiaccount.api.service.WxConfigService;

@Service
public class WxConfigServiceImpl implements WxConfigService {
    private final ThreadLocal<String> localAppId = new ThreadLocal<>();

    @Autowired
    private RedissonClient redissonClient;

    @Value("${weixin.redissonKeyPrefix}")
    private String redissonKeyPrefix;
    @Value("${weixin.offiaccount.getTokenUrl}")
    private String getTokenUrl;

    @Override
    public WxConfigDTO get(String appId) {
        RBucket<String> redissonClientBucket = redissonClient.getBucket(redissonKeyPrefix + "config_" + appId);
        if (redissonClientBucket.isExists()) {
            String json = redissonClientBucket.get();
            return WxConfigDTO.fromJson(json);
        }
        return null;
    }

    @Override
    public void set(WxConfigDTO wxConfigDTO) {
        RBucket<String> redissonClientBucket = redissonClient
                .getBucket(redissonKeyPrefix + "config_" + wxConfigDTO.getAppId());
        if (StringUtils.isBlank(wxConfigDTO.getGetTokenUrl())) {
            wxConfigDTO.setGetTokenUrl(getTokenUrl);
        }
        redissonClientBucket.set(JSONObject.toJSONString(wxConfigDTO));
    }

    @Override
    public void setLocalAppId(String appId) {
        localAppId.set(appId);
    }

    @Override
    public String getLocalAppId() {
        return localAppId.get();
    }

    @Override
    public void removeLocalAppId() {
        localAppId.remove();
    }
}
