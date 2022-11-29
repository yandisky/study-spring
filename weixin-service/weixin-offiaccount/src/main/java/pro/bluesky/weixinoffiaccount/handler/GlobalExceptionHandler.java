package pro.bluesky.weixinoffiaccount.handler;

import com.alibaba.fastjson.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pro.bluesky.weixinoffiaccount.common.ResponseCodeConst;
import pro.bluesky.weixinoffiaccount.common.ResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseDTO exceptionHandler(Exception e) {
        log.error("【全局异常处理】", e);

        // http 请求方式错误
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseDTO.error(ResponseCodeConst.REQUEST_METHOD_ERROR);
        }

        // 参数类型错误
        if (e instanceof TypeMismatchException) {
            return ResponseDTO.error(ResponseCodeConst.ERROR_PARAM);
        }

        // 请求参数JSON格式错误
        if (e instanceof HttpMessageNotReadableException) {
            return ResponseDTO.error(ResponseCodeConst.JSON_FORMAT_ERROR);
        }

        // 参数校验未通过
        if (e instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
            return ResponseDTO.error(ResponseCodeConst.ERROR_PARAM, String.join(",", msgList));
        }

        // 微信接口返回JSON格式错误
        if (e instanceof JSONException) {
            return ResponseDTO.error(ResponseCodeConst.WEIXIN_JSON_FORMAT_ERROR);
        }

        return ResponseDTO.error(ResponseCodeConst.SYSTEM_ERROR);
    }
}
