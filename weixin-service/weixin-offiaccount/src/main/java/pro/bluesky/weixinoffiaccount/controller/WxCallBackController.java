package pro.bluesky.weixinoffiaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.bluesky.weixinoffiaccount.service.WxCallBackService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wx")
public class WxCallBackController {
    @Autowired
    private WxCallBackService wxCallBackService;

    /**
     * 微信回调验证
     */
    @GetMapping("/callback/{appId}")
    public String callback(@PathVariable String appId, HttpServletRequest request) {
        return wxCallBackService.verifyURL(appId, request);
    }

    /**
     * 微信回调消息
     */
    @PostMapping("/callback/{appId}")
    public String callback(@PathVariable String appId, @RequestBody String postData, HttpServletRequest request) {
        return wxCallBackService.receiveMsg(appId, postData, request);
    }
}
