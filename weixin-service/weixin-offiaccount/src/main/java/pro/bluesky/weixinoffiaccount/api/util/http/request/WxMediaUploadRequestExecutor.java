package pro.bluesky.weixinoffiaccount.api.util.http.request;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.response.ResponseHandler;

import java.io.File;
import java.io.IOException;

/**
 * 微信素材上传的http请求执行器
 */
public class WxMediaUploadRequestExecutor implements RequestExecutor<String, File> {
    private static final Logger log = LoggerFactory.getLogger(WxMediaUploadRequestExecutor.class);

    @Override
    public String execute(String url, File data, ResponseHandler<String> responseHandler) throws WxErrorException {
        HttpPost httpPost = new HttpPost(url);
        HttpEntity requestEntity = MultipartEntityBuilder.create().addBinaryBody("media", data)
                .setMode(HttpMultipartMode.RFC6532).build();
        httpPost.setEntity(requestEntity);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String result = null;
        log.info("【素材上传】: 请求链接,{}", url);
        try {
            httpResponse = httpClient.execute(httpPost);
            return responseHandler.handle(httpResponse);
        } catch (IOException e) {
            log.error("【素材上传】: 处理异常,{}", url, e);
            throw new WxErrorException(e);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                log.error("【素材上传】: 关闭httpResponse异常,{}", url, e);
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("【素材上传】: 关闭httpClient异常,{}", url, e);
            }
        }
    }
}
