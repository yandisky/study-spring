package pro.bluesky.weixinoffiaccount.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;
import pro.bluesky.weixinoffiaccount.api.exception.WxError;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.service.WxConfigService;
import pro.bluesky.weixinoffiaccount.api.service.WxRequestService;
import pro.bluesky.weixinoffiaccount.api.util.http.request.RequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.ResponseHandler;
import pro.bluesky.weixinoffiaccount.api.util.http.request.WxGetRequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.WxResponseHandler;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class WxRequestServiceImpl implements WxRequestService {
    private static final Logger log = LoggerFactory.getLogger(WxRequestServiceImpl.class);

    @Autowired
    private WxConfigService wxConfigService;
    @Autowired
    private RedissonClient redissonClient;

    @Value("${weixin.redissonKeyPrefix}")
    private String redissonKeyPrefix;

    private int maxRetryTimes = 3;

    @Override
    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    @Override
    public <T, E> T execute(RequestExecutor<T, E> executor, String url, E data, ResponseHandler<T> responseHandler) throws WxErrorException {
        int retryTimes = 0;
        do {
            try {
                return this.executeInternal(executor, url, data, responseHandler);
            } catch (WxErrorException e) {
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("重试达到最大次数，({}次)", maxRetryTimes, e);
                    //最后一次重试失败后，直接抛出异常，不再等待
                    throw new WxErrorException("微信服务端异常，超出重试次数");
                }
                WxError error = e.getError();
                //针对WxError为-1的情况，进行重试请求
                if (error.getErrCode() == -1) {
                    int sleepMillis = 1000 * (1 << retryTimes);//延迟重试累积时间,以1000ms为累积单位
                    try {
                        log.warn("微信系统繁忙，{}ms后重试(第{}次)", sleepMillis, retryTimes + 1, e);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new WxErrorException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (retryTimes++ < maxRetryTimes);
        log.warn("重试达到最大次数，({}次)", maxRetryTimes);
        throw new WxErrorException("微信服务端异常，超出重试次数");
    }

    private <T, E> T executeInternal(RequestExecutor<T, E> executor, String url, E data, ResponseHandler<T> responseHandler) throws WxErrorException {
        String accessToken = getAccessToken();
        String urlWithAccessToken = url + (url.contains("?") ? "&" : "?") + "access_token=" + accessToken;
        try {
            return executor.execute(urlWithAccessToken, data, responseHandler);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            log.error("\n【请求地址】: {}\n【请求参数】: {}\n【错误信息】：{}", urlWithAccessToken, data, error);
            //判断accessToken失效的情况，先清除accessToken缓存再重新请求
            if (error.getErrCode() == 40001 || error.getErrCode() == 40014 || error.getErrCode() == 42001) {
                log.warn("微信access_token失效，错误代码：{}，错误信息：{}", error.getErrCode(), error.getErrMsg());
                //判断accessToken是否已更新，已更新则无需删除有效accessToken
                RBucket<String> redissonClientBucket = redissonClient.getBucket(redissonKeyPrefix + "accessToken_" + wxConfigService.getLocalAppId());
                String refreshAccessToken = null;
                if (redissonClientBucket.isExists()) {
                    refreshAccessToken = redissonClientBucket.get();
                }
                if (accessToken.equals(refreshAccessToken)) {
                    redissonClientBucket.delete();
                }
                return this.execute(executor, url, data, responseHandler);
            }
            if (error.getErrCode() != 0) {
                throw new WxErrorException(error, e);
            }
            return null;
        } catch (Exception e) {
            log.error("\n【请求地址】: {}\n【请求参数】: {}\n【错误信息】：{}", urlWithAccessToken, data, e.getMessage());
            throw new WxErrorException(e);
        }
    }

    private String getAccessToken() throws WxErrorException {
        String appId = wxConfigService.getLocalAppId();
        WxConfigDTO wxConfigDTO = wxConfigService.get(appId);
        if (wxConfigDTO == null) {
            throw new WxErrorException("微信配置参数缺失,appId:" + appId);
        }
        RBucket<String> redissonClientBucket = redissonClient.getBucket(redissonKeyPrefix + "accessToken_" + appId);
        if (!redissonClientBucket.isExists()) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("appid", appId);
            data.put("secret", wxConfigDTO.getAppSecret());
            WxGetRequestExecutor wxGetRequestExecutor = new WxGetRequestExecutor();
            String result = wxGetRequestExecutor.execute(wxConfigDTO.getGetTokenUrl(), data, new WxResponseHandler());
            JSONObject jsonObject = JSONObject.parseObject(result);
            Long expiresIn;
            if (jsonObject.getString("access_token") == null || (expiresIn = jsonObject.getLong("expires_in")) == null) {
                throw new WxErrorException("微信返回access_token异常：" + result);
            }
            redissonClientBucket.set(jsonObject.getString("access_token"), expiresIn, TimeUnit.SECONDS);
        }
        return redissonClientBucket.get();
    }
}
