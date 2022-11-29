package pro.bluesky.weixinoffiaccount.api.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;
import pro.bluesky.weixinoffiaccount.api.util.FileDownloadUtil;
import pro.bluesky.weixinoffiaccount.api.util.http.request.WxPostRequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.WxMediaDownloadResponseHandler;
import pro.bluesky.weixinoffiaccount.api.util.http.request.WxMediaUploadRequestExecutor;
import pro.bluesky.weixinoffiaccount.api.util.http.response.WxResponseHandler;

import java.io.File;

@Service
public class WxMediaService {
    @Autowired
    private WxRequestService wxRequestService;

    /**
     * 新增微信素材，包括临时素材，永久素材
     *
     * @param wxApiUrl 微信api链接
     * @param type     微信素材类型
     * @param fileUrl  文件链接
     * @return
     * @throws WxErrorException
     */
    public String uploadMedia(String wxApiUrl, String type, String fileUrl) throws WxErrorException {
        FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();
        File file = null;
        try {
            file = fileDownloadUtil.downloadTempFile(fileUrl, 10 * 1024 * 1024);
            if (file == null) {
                throw new WxErrorException("创建微信素材文件失败");
            }
            wxApiUrl = wxApiUrl + (wxApiUrl.contains("?") ? "&" : "?") + "type=" + type;
            return wxRequestService.execute(new WxMediaUploadRequestExecutor(), wxApiUrl, file, new WxResponseHandler());
        } catch (Exception e) {
            if (e instanceof WxErrorException) {
                throw (WxErrorException) e;
            }
            throw new WxErrorException(e);
        } finally {
            //删除产生临时文件
            if (file != null) {
                file.delete();
            }
        }
    }

    /**
     * 获取微信素材，包括临时素材，永久素材
     *
     * @param wxApiUrl 微信api
     * @param mediaId  素材media_id
     * @return
     * @throws WxErrorException
     */
    public String getMedia(String wxApiUrl, String mediaId) throws WxErrorException {
        if (wxApiUrl.contains("material/get_material")) {
            JSONObject data = new JSONObject();
            data.put("media_id", mediaId);
            return wxRequestService.execute(new WxPostRequestExecutor(), wxApiUrl, data, new WxMediaDownloadResponseHandler());
        } else {
            wxApiUrl = wxApiUrl + (wxApiUrl.contains("?") ? "&" : "?") + "media_id=" + mediaId;
            return wxRequestService.execute(new WxPostRequestExecutor(), wxApiUrl, null, new WxMediaDownloadResponseHandler());
        }
    }
}
