package stephen.springboot.mailservice.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import stephen.springboot.mailservice.common.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * IP过滤器
 */
public class IpInterceptor implements HandlerInterceptor {
    private List<String> ipList = new ArrayList<>();

    public IpInterceptor(List<String> ipList) {
        this.ipList = ipList;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(ipList.size() + " "  + ipList.get(0));
        if(!ipList.contains(request.getRemoteAddr())){
            throw new BusinessException("IP白名单限制");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
