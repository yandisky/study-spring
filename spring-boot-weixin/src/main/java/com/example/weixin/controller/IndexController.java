package com.example.weixin.controller;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() throws AesException {
        //https://127.0.0.1/suite/receive?msg_signature=3a7b08bb8e6dbce3c9671d6fdb69d15066227608&timestamp=1403610513&nonce=380320359
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt("oOU4DJIE49vJj1", "Swe71JTWs4q1mz7YxtzvWwaRwChObCViOTW67Cbihri", "wwc7342fa63c523b9a");
        String sReqMsgSig = "3a7b08bb8e6dbce3c9671d6fdb69d15066227608";
        String sReqTimeStamp = "1403610513";
        String sReqNonce = "380320359";
        String sReqData = "<xml><ToUserName><![CDATA[tjfc481f8cafd78d48]]></ToUserName><Encrypt><![CDATA[REczfveef74LTAc9+/n+C/S9kmWZdQ9x7/OCp8RbLSNFGSHi5JQDxBCuYHzi4BqwzBYY96jQ4Viy82H3++dFezTYsb7/BbUZ05cIGPWxR9nDKJX1AvUbxKZp817ZPWuw/MmvR0f1r/QMUfbPIhYH39A5qEvQWEvQlCfj9Hzi0h69lNLL2B2wF22OgPOQqFFyIBIkyj7lO9gPVECnnmapXiqNJfHInJB1ajrf0UGI+zYAQSGahkhcGnvm19Nxtsbd4AjBLfgkaD/yv53JOX14/ljpOxO8fjipCeYZASRaEgnreUUwTtLN0/oTsV9bZD3DCboknew7KGYy9f3qE0GefeAR/HxLTMDOfTxhneOkXfAZwsEKmozqlVj3+1Fxrise]]></Encrypt><AgentID><![CDATA[]]></AgentID></xml>";
        return wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
    }
}
