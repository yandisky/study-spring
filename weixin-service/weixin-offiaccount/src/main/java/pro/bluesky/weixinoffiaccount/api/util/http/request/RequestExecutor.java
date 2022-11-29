package pro.bluesky.weixinoffiaccount.api.util.http.request;

import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.response.ResponseHandler;

/**
 * http请求执行器
 *
 * @param <T> 返回值类型
 * @param <E> 请求参数类型
 */
public interface RequestExecutor<T, E> {
    /**
     * 执行http请求.
     *
     * @param url             url
     * @param data            数据
     * @param responseHandler http响应处理器
     * @return 响应结果
     * @throws WxErrorException
     */
    T execute(String url, E data, ResponseHandler<T> responseHandler) throws WxErrorException;
}
