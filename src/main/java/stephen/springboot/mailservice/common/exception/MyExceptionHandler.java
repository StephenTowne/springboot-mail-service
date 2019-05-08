package stephen.springboot.mailservice.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import stephen.springboot.mailservice.common.response.ResponseResult;
import stephen.springboot.mailservice.common.response.ResponseResultGenerator;

/**
 * 基于controllerAdvice的统一异常处理
 * 分别捕获自定义异常和其他异常
 *
 * 注意：无法捕获spring 404异常，需要额外实现ErrorController处理
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler({BusinessException.class}) //指定拦截异常的类型
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //自定义浏览器返回状态码
    public ResponseResult businessExceptionHandler(BusinessException e) {
        return new ResponseResult()
                .setCode(e.getCode())
                .setMsg(e.getMsg());
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //自定义浏览器返回状态码
    public ResponseResult exceptionHandler(Throwable e) {
        if(e instanceof javax.persistence.EntityNotFoundException){
            return ResponseResultGenerator.genFailResult("数据不存在");
        }
        log.error(e.getMessage(), e);
        return ResponseResultGenerator.genFailResult();
    }
}

