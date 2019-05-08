package stephen.springboot.mailservice.common.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stephen.springboot.mailservice.common.response.ResponseResult;
import stephen.springboot.mailservice.common.response.ResponseResultGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 404 not found处理
 *
 * 如果不需要静态资源访问，可以直接配置
 * spring:
 *     mvc:
 *       throw-exception-if-no-handler-found: true
 *     resources:
 *       add-mappings: false
 */
@RestController
public class NotFoundController implements ErrorController {
    // 错误地址
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(ERROR_PATH)
    public ResponseResult error(HttpServletRequest request, HttpServletResponse response){
        return ResponseResultGenerator.genFailResult();
    }
}
