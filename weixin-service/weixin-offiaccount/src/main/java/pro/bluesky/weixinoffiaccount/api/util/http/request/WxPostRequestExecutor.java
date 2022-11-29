package pro.bluesky.weixinoffiaccount.api.util.http.request;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.response.ResponseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WxPostRequestExecutor implements RequestExecutor<String, JSONObject> {
    private static final Logger log = LoggerFactory.getLogger(WxPostRequestExecutor.class);

    @Override
    public String execute(String url, JSONObject data, ResponseHandler<String> responseHandler) throws WxErrorException {
        log.info("【SimplePost】: {}\n【请求参数】: {}", url, data);
        HttpPost httpPost = new HttpPost(url);
        if (data != null) {
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(data.toJSONString(), StandardCharsets.UTF_8));
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            return responseHandler.handle(httpResponse);
        } catch (IOException e) {
            log.error("【SimplePost】: 处理异常,{}", url, e);
            throw new WxErrorException(e);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                log.error("【SimplePost】: 关闭httpResponse异常,{}", url, e);
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("【SimplePost】: 关闭httpClient异常,{}", url, e);
            }
        }
    }
}
