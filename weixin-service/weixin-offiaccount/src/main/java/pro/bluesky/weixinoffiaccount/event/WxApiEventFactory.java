package pro.bluesky.weixinoffiaccount.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信api转接工厂，对微信请求api进行配置转接处理
 */
@Component
public class WxApiEventFactory {
    /**
     * 微信api匹配路由
     */
    private final static Map<String, String> eventRoute = new HashMap<String, String>() {
        {
            //创建二维码ticket
            put("qrcode/create", "wxApiEventQrcodeImpl");
            //客服接口-发消息
            put("message/custom/send", "wxApiEventMessageImpl");
            //上传临时素材
            put("media/upload", "wxApiEventMediaUploadImpl");
            //上传永久素材
            put("material/add_material", "wxApiEventMediaUploadImpl");
            //获取临时素材
            put("media/get", "wxApiEventMediaGetImpl");
            //获取永久素材
            put("material/get_material", "wxApiEventMediaGetImpl");
        }
    };

    @Autowired
    private Map<String, WxApiEvent> wxApiEventMap;

    public WxApiEvent factory(String wxApiUrl) {
        for (String key : eventRoute.keySet()) {
            if (wxApiUrl.contains(key)) {
                String eventClass = eventRoute.get(key);
                if (wxApiEventMap.containsKey(eventClass)) {
                    return wxApiEventMap.get(eventClass);
                }
            }
        }
        //返回默认处理
        return wxApiEventMap.get("wxApiEventImpl");
    }
}
