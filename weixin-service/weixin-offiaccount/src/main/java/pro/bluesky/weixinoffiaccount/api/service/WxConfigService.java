package pro.bluesky.weixinoffiaccount.api.service;

import pro.bluesky.weixinoffiaccount.api.dto.WxConfigDTO;

public interface WxConfigService {
    public WxConfigDTO get(String appId);

    public void set(WxConfigDTO wxConfigDTO);

    public void setLocalAppId(String appId);

    public String getLocalAppId();

    public void removeLocalAppId();
}
