package pro.bluesky.weixinoffiaccount.api.util.http.response;

import org.apache.http.client.methods.CloseableHttpResponse;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;

/**
 * http响应处理器
 *
 * @param <T> 返回值类型
 */
public interface ResponseHandler<T> {
    /**
     * 响应结果处理.
     *
     * @param response CloseableHttpResponse
     */
    T handle(CloseableHttpResponse response) throws WxErrorException;
}
