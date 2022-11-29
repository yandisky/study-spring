package pro.bluesky.weixinoffiaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pro.bluesky.weixinoffiaccount.common.ResponseDTO;
import pro.bluesky.weixinoffiaccount.service.WxApiService;

@RestController
@RequestMapping("/wx")
public class WxApiController {
    @Autowired
    private WxApiService wxApiService;

    @PostMapping("/config")
    public ResponseDTO config(@RequestBody String postData) {
        return wxApiService.config(postData);
    }

    @PostMapping("/api/{appId}")
    public ResponseDTO api(@PathVariable String appId, @RequestBody String postData) {
        return wxApiService.api(appId, postData);
    }
}
