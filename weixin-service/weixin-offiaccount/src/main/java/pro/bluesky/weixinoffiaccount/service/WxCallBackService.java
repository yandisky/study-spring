package pro.bluesky.weixinoffiaccount.service;

import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;
import pro.bluesky.weixinoffiaccount.api.service.WxConfigService;
import pro.bluesky.weixinoffiaccount.event.WxCallBackEvent;
import pro.bluesky.weixinoffiaccount.event.WxCallBackEventFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class WxCallBackService {
    private static final Logger log = LoggerFactory.getLogger(WxCallBackService.class);

    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private WxConfigService wxConfigService;

    @Autowired
    private WxCallBackEventFactory wxCallBackEventFactory;

    @PostConstruct
    void init() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000));
        }
    }

    @PreDestroy
    void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            threadPoolExecutor = null;
        }
    }

    public String verifyURL(String appId, HttpServletRequest request) {
        String echoStr = request.getParameter("echostr");
        WxConfigDTO wxConfigDTO = wxConfigService.get(appId);
        if (wxConfigDTO == null) {
            log.error("【微信回调验证】: {},微信配置参数缺失", appId);
            return echoStr;
        }
        String encryptType = request.getParameter("encrypt_type");
        //判断是否需要解密,明文直接返回
        if (!"aes".equals(encryptType)) {
            return echoStr;
        }
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        try {
            log.info("【微信回调验证】: {},验证参数,{},{},{},{},{},{}", appId, signature, timestamp, nonce, echoStr,
                    wxConfigDTO.getToken(), wxConfigDTO.getEncodingAESKey());
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(wxConfigDTO.getToken(), wxConfigDTO.getEncodingAESKey(),
                    appId);
            return wxBizMsgCrypt.verifyUrl(signature, timestamp, nonce, echoStr);
        } catch (AesException e) {
            log.error("【微信回调验证】: {},验证失败", appId, e);
        }
        return echoStr;
    }

    public String receiveMsg(String appId, String postData, HttpServletRequest request) {
        WxConfigDTO wxConfigDTO = wxConfigService.get(appId);
        if (wxConfigDTO == null) {
            log.error("【微信回调消息】: {},微信配置参数缺失", appId);
            return "";
        }
        String xml = postData;
        WXBizMsgCrypt wxBizMsgCrypt = null;
        try {
            String encryptType = request.getParameter("encrypt_type");
            //判断是否需要解密，加密情况下需要解密消息内容
            if ("aes".equals(encryptType)) {
                String signature = request.getParameter("msg_signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                log.info("【微信回调消息】: {},验证参数,{},{},{},{},{},{}", appId, signature, timestamp, nonce,
                        wxConfigDTO.getToken(), wxConfigDTO.getEncodingAESKey(), postData);
                wxBizMsgCrypt = new WXBizMsgCrypt(wxConfigDTO.getToken(), wxConfigDTO.getEncodingAESKey(), appId);
                xml = wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, postData);
            }
            log.info("【微信回调消息】: {},接收数据转换前{}", appId, xml);
            JSONObject jsonObject = xmlToJson(xml);
            log.info("【微信回调消息】: {},接收数据转换后{}", appId, jsonObject.toJSONString());
            //对微信回调消息进行配置转接处理
            String msgType = jsonObject.getString("MsgType");
            if (msgType == null) {
                log.error("【微信回调消息】: {},接收数据MsgType错误", appId);
                return "";
            }
            WxCallBackEvent wxCallBackEvent = wxCallBackEventFactory.factory(msgType);
            if (wxCallBackEvent != null) {
                threadPoolExecutor.execute(() -> wxCallBackEvent.handle(wxConfigDTO, jsonObject));
            }
            if (wxBizMsgCrypt != null) {
                return wxBizMsgCrypt.encryptMsg(getReturnXml("success"), "", "");
            }
            return "success";
        } catch (Exception e) {
            log.error("【微信回调消息】: {},处理失败", appId, e);
        }
        return "";
    }

    private JSONObject xmlToJson(String xml) throws Exception {
        JSONObject jsonObject = new JSONObject();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        StringReader stringReader = new StringReader(xml);
        InputSource inputSource = new InputSource(stringReader);
        Document document = documentBuilder.parse(inputSource);
        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                String nodeText = node.getTextContent();
                if ("".equals(nodeText) || "xml".equals(nodeName) || "URL".equals(nodeName)) {
                    continue;
                }
                if ("CreateTime".equals(nodeName)) {
                    jsonObject.put(nodeName, Long.parseLong(nodeText) * 1000);
                } else if ("MsgId".equals(nodeName)) {
                    jsonObject.put(nodeName, Long.valueOf(nodeText));
                } else {
                    jsonObject.put(nodeName, nodeText);
                }
            }
        }
        return jsonObject;
    }

    private String getReturnXml(String content) {
        return "<xml><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[" + content + "]]></Content></xml>";
    }
}
