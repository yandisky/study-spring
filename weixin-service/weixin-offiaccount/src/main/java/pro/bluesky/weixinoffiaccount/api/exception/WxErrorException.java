package pro.bluesky.weixinoffiaccount.api.exception;

/**
 * 微信错误异常
 */
public class WxErrorException extends Exception {
    private static final long serialVersionUID = 2700512023965541217L;

    private final WxError error;

    public WxErrorException(String message) {
        this(new WxError(-1, message, null));
    }

    public WxErrorException(WxError error) {
        super(error.toString());
        this.error = error;
    }

    public WxErrorException(WxError error, Throwable cause) {
        super(error.toString(), cause);
        this.error = error;
    }

    public WxErrorException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.error = new WxError(-1, cause.getMessage(), null);
    }

    public WxError getError() {
        return this.error;
    }
}
