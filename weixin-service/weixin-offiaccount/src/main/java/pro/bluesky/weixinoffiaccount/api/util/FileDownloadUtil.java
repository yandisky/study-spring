package pro.bluesky.weixinoffiaccount.api.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class FileDownloadUtil {
    private static final Logger log = LoggerFactory.getLogger(FileDownloadUtil.class);

    /**
     * 根据远程文件地址，生成本地临时文件，File使用完毕后需要执行删除
     *
     * @param fileUrl       远程图片地址
     * @param fileSizeLimit 文件大小限制
     * @return File
     */
    public File downloadTempFile(String fileUrl, long fileSizeLimit) throws Exception {
        HttpGet httpGet = new HttpGet(fileUrl);
        CloseableHttpClient httpClient = wrapClient();
        CloseableHttpResponse httpResponse = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        log.info("【文件下载】: 请求链接,{}", fileUrl);
        try {
            httpResponse = httpClient.execute(httpGet);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                HttpEntity responseEntity = httpResponse.getEntity();
                if (responseEntity != null) {
                    if (responseEntity.getContentLength() > fileSizeLimit) {
                        throw new Exception("文件大小不能超过" + (fileSizeLimit / 1024 / 1024) + "M");
                    }
                    String fileExt = null;
                    Header header = httpResponse.getFirstHeader("Content-disposition");
                    if (header != null) {
                        String contentDisposition = URLDecoder.decode(header.getValue(), "UTF-8");
                        String filename = contentDisposition.substring(contentDisposition.indexOf("filename=\"") + 10,
                                contentDisposition.length() - 1);
                        log.info("【文件下载】: 文件名称,{}", filename);
                        fileExt = FilenameUtils.getExtension(filename);
                    }
                    if (StringUtils.isBlank(fileExt)) {
                        fileExt = FilenameUtils.getExtension(fileUrl);
                    }
                    inputStream = responseEntity.getContent();
                    //将 inputStream 转换 File
                    file = File.createTempFile("weixin-offiaccount-file", "." + fileExt);
                    outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] bytes = new byte[2 * 1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                }
            }
            return file;
        } catch (Exception e) {
            log.error("【文件下载】: 处理异常,{}", fileUrl, e);
            throw e;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("【文件下载】: 关闭输入流异常,{}", fileUrl, e);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("【文件下载】: 关闭输出流异常,{}", fileUrl, e);
            }
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                log.error("【文件下载】: 关闭httpResponse异常,{}", fileUrl, e);
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("【文件下载】: 关闭httpClient异常,{}", fileUrl, e);
            }
        }
    }

    //绕过ssl证书验证
    private static CloseableHttpClient wrapClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSLv3");
            X509TrustManager trustManager = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{trustManager}, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(ssf).build();
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }
}
