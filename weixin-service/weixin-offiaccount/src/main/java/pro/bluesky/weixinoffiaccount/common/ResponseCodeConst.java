package pro.bluesky.weixinoffiaccount.common;

public class ResponseCodeConst {
    public static final ResponseCodeConst SUCCESS = new ResponseCodeConst(200, "操作成功");

    public static final ResponseCodeConst ERROR_PARAM = new ResponseCodeConst(500, "参数异常");

    public static final ResponseCodeConst SYSTEM_ERROR = new ResponseCodeConst(500, "系统错误");

    public static ResponseCodeConst REQUEST_METHOD_ERROR = new ResponseCodeConst(500, "请求方式错误");

    public static ResponseCodeConst REQUEST_PARAM_ERROR = new ResponseCodeConst(500, "微信配置参数错误");

    public static ResponseCodeConst JSON_FORMAT_ERROR = new ResponseCodeConst(500, "请求参数JSON格式错误");

    public static ResponseCodeConst WEIXIN_ERROR = new ResponseCodeConst(500, "微信接口返回错误");

    public static ResponseCodeConst WEIXIN_JSON_FORMAT_ERROR = new ResponseCodeConst(500, "微信接口返回JSON格式错误");

    private int status;
    private String msg;

    public ResponseCodeConst() {
    }

    public ResponseCodeConst(int status) {
        this.status = status;
    }

    public ResponseCodeConst(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
