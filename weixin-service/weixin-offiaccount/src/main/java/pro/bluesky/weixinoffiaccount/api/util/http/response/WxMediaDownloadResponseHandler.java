package pro.bluesky.weixinoffiaccount.api.util.http.response;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import pro.bluesky.weixinoffiaccount.api.exception.WxError;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

public class WxMediaDownloadResponseHandler implements ResponseHandler<String> {

    @Override
    public String handle(CloseableHttpResponse httpResponse) throws WxErrorException {
        String result = null;
        int code = httpResponse.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK) {
            HttpEntity responseEntity = httpResponse.getEntity();
            if (responseEntity != null) {
                //由于永久素材与临时素材返回header不一致，所以使用Content-disposition获取文件信息
                Header header = httpResponse.getFirstHeader("Content-disposition");
                if (header != null) {
                    String contentDisposition;
                    try {
                        contentDisposition = URLDecoder.decode(header.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new WxErrorException(e);
                    }
                    String filename = contentDisposition.substring(contentDisposition.indexOf("filename=\"") + 10,
                            contentDisposition.length() - 1);
                    //将微信返回文件转换base64
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fileName", filename);
                    jsonObject.put("fileType", FilenameUtils.getExtension(filename));
                    try {
                        jsonObject.put("fileBase64",
                                Base64.getEncoder().encodeToString(EntityUtils.toByteArray(responseEntity)));
                    } catch (IOException e) {
                        throw new WxErrorException(e);
                    }
                    result = jsonObject.toJSONString();
                } else {
                    try {
                        result = EntityUtils.toString(responseEntity, Consts.UTF_8);
                    } catch (IOException e) {
                        throw new WxErrorException(e);
                    }
                    if (StringUtils.isBlank(result)) {
                        throw new WxErrorException("微信返回数据异常");
                    }
                    WxError error = WxError.fromJson(result);
                    if (error.getErrCode() != 0) {
                        throw new WxErrorException(error);
                    }
                }
            }
        }
        if (StringUtils.isBlank(result)) {
            throw new WxErrorException("微信返回数据异常");
        }
        return result;
    }
}
