package pro.bluesky.weixinoffiaccount.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信回调转接工厂，对微信回调消息进行配置转接处理
 */
@Component
public class WxCallBackEventFactory {
    /**
     * 微信回调消息匹配路由
     */
    private final static Map<String, String> eventRoute = new HashMap<String, String>();

    @Autowired
    private Map<String, WxCallBackEvent> wxCallBackEventMap;

    public WxCallBackEvent factory(String msgType) {
        for (String key : eventRoute.keySet()) {
            if (msgType.contains(key)) {
                String eventClass = eventRoute.get(key);
                if (wxCallBackEventMap.containsKey(eventClass)) {
                    return wxCallBackEventMap.get(eventClass);
                }
            }
        }
        //返回默认处理
        return wxCallBackEventMap.get("wxCallBackEventImpl");
    }
}
