package pro.bluesky.weixinoffiaccount.api.util.http.response;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import pro.bluesky.weixinoffiaccount.api.exception.WxError;
import pro.bluesky.weixinoffiaccount.api.exception.WxErrorException;

import java.io.IOException;

public class WxResponseHandler implements ResponseHandler<String> {

    @Override
    public String handle(CloseableHttpResponse httpResponse) throws WxErrorException {
        String result = null;
        int code = httpResponse.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK) {
            HttpEntity responseEntity = httpResponse.getEntity();
            if (responseEntity != null) {
                try {
                    result = EntityUtils.toString(responseEntity, Consts.UTF_8);
                } catch (IOException e) {
                    throw new WxErrorException(e);
                }
            }
        }
        if (StringUtils.isBlank(result)) {
            throw new WxErrorException("微信返回数据异常");
        }
        WxError error = WxError.fromJson(result);
        if (error.getErrCode() != 0) {
            throw new WxErrorException(error);
        }
        return result;
    }
}
