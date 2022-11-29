package pro.bluesky.weixinoffiaccount.common;

public class ResponseDTO<T> {
    private Integer status;
    private String msg;
    private T data;

    public ResponseDTO() {
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst) {
        this.status = responseCodeConst.getStatus();
        this.msg = responseCodeConst.getMsg();
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, String msg) {
        this.status = responseCodeConst.getStatus();
        this.msg = responseCodeConst.getMsg() + "," + msg;
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, T data) {
        this.status = responseCodeConst.getStatus();
        this.msg = responseCodeConst.getMsg();
        this.data = data;
    }

    public ResponseDTO(ResponseCodeConst responseCodeConst, T data, String msg) {
        this.status = responseCodeConst.getStatus();
        this.msg = responseCodeConst.getMsg() + "," + msg;
        this.data = data;
    }

    public static <T> ResponseDTO<T> success() {
        return new ResponseDTO(ResponseCodeConst.SUCCESS);
    }

    public static <T> ResponseDTO success(String msg) {
        return new ResponseDTO(ResponseCodeConst.SUCCESS, msg);
    }

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO(ResponseCodeConst.SUCCESS, data);
    }

    public static <T> ResponseDTO<T> error(ResponseCodeConst codeConst) {
        return new ResponseDTO<>(codeConst);
    }

    public static <T> ResponseDTO<T> error(ResponseCodeConst codeConst, String msg) {
        return new ResponseDTO<T>(codeConst, msg);
    }

    public static <T> ResponseDTO<T> error(ResponseCodeConst codeConst, T data) {
        return new ResponseDTO<T>(codeConst, data);
    }

    public static <T> ResponseDTO<T> error(ResponseCodeConst codeConst, T data, String msg) {
        return new ResponseDTO<T>(codeConst, data, msg);
    }

    public Integer getStatus() {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
