package pro.bluesky.weixinoffiaccount.api.util.http.request;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.response.ResponseHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WxGetRequestExecutor implements RequestExecutor<String, HashMap<String, Object>> {
    private static final Logger log = LoggerFactory.getLogger(WxGetRequestExecutor.class);

    @Override
    public String execute(String url, HashMap<String, Object> data, ResponseHandler<String> responseHandler) throws WxErrorException {
        log.info("【SimpleGet】: {}\n【请求参数】: {}", url, data);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(url);
            if (data != null && !data.isEmpty()) {
                Iterator iterator = data.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
                    if (entry != null && entry.getValue() != null) {
                        stringBuilder.append(stringBuilder.indexOf("?") >= 0 ? "&" : "?").append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    }
                }
            }
            HttpGet httpGet = new HttpGet(stringBuilder.toString());
            httpResponse = httpClient.execute(httpGet);
            return responseHandler.handle(httpResponse);
        } catch (IOException e) {
            log.error("【SimpleGet】: 处理异常,{}", url, e);
            throw new WxErrorException(e);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                log.error("【SimpleGet】: 关闭httpResponse异常,{}", url, e);
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("【SimpleGet】: 关闭httpClient异常,{}", url, e);
            }
        }
    }
}