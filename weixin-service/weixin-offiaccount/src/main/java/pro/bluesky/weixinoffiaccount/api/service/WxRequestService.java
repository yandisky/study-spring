package pro.bluesky.weixinoffiaccount.api.service;

import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.http.request.RequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.ResponseHandler;

public interface WxRequestService {
    /**
     * 重试请求，设置重试次数
     *
     * @param maxRetryTimes 重试次数
     */
    void setMaxRetryTimes(int maxRetryTimes);

    /**
     * 执行请求
     *
     * @param executor        http请求执行器
     * @param url             请求url
     * @param data            请求参数
     * @param responseHandler http响应处理器
     * @param <T>             返回响应类型
     * @param <E>             请求参数类型
     * @return 响应结果
     * @throws WxErrorException
     */
    <T, E> T execute(RequestExecutor<T, E> executor, String url, E data, ResponseHandler<T> responseHandler) throws WxErrorException;
}
