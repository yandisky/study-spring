package com.example.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

public class TokenFilter extends ZuulFilter {
    /**
     * 过滤器的类型，它决定过滤器在请求的哪个生命周期中执行。
     * FilterConstants.PRE_TYPE：代表会在请求被路由之前执行。
     * PRE、ROUTING、POST、ERROR
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * filter执行顺序，通过数字指定。[数字越大，优先级越低]
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断该过滤器是否需要被执行。这里我们直接返回了true，因此该过滤器对所有请求都会生效。
     * 实际运用中我们可以利用该函数来指定过滤器的有效范围。
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /*
     * 具体执行逻辑
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContent = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = requestContent.getRequest();
        String token = httpServletRequest.getParameter("token");
        if (token == null || token.isEmpty()) {
            requestContent.setSendZuulResponse(false);
            requestContent.setResponseStatusCode(401);
            requestContent.setResponseBody("token is empty");
        }
        return null;
    }
}
