package stephen.springboot.mailservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.spring.web.json.Json;
import stephen.springboot.mailservice.common.GlobalVar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    private static final String SEPARATOR = "|";
    private static final String EMPTY_STR = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 清除MDC信息和requestID，避免多进程影响
        MDC.clear();
        GlobalVar.removeRequestId();

        MDC.put("requestId", GlobalVar.getRequestId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        //domain & uri & refer
        String domain = request.getServerName();
        String uri = request.getRequestURI();
        String refer = request.getHeader("Referer");
        String remoteIp = getRemoteAddr(request);
        String userAgent = request.getHeader("User-Agent");
        Map<String, String> paramPair = getRequestParamValueMap(request);

        printAccesslog(remoteIp, domain, uri, refer, userAgent, paramPair);
    }

    //打印access log
    private void printAccesslog(String remoteIp, String domain, String uri, String refer, String userAgent, Map<String, String> paramPair) {

        StringBuilder sb = new StringBuilder();

        long timestamp = System.currentTimeMillis();
        sb.append(SEPARATOR).append(timestamp);
        sb.append(SEPARATOR).append(remoteIp);
        sb.append(SEPARATOR).append(GlobalVar.getRequestId());
        sb.append(SEPARATOR).append(domain);
        sb.append(SEPARATOR).append(uri);
        sb.append(SEPARATOR).append(refer);
        sb.append(SEPARATOR).append(userAgent);
        //将参数map打印成json格式，利于统计分析
        //sb.append(SEPARATOR).append(J(paramPair));

        log.info(sb.toString());
    }

    private String getRemoteAddr(HttpServletRequest request) {
        if (log.isDebugEnabled()) {// 打印所有日志
            log.debug("X-Forwarded-For:" + request.getHeader
                    ("X-Forwarded-For") +
                    "\tProxy-Client-IP:" + request.getHeader("Proxy-Client-IP") +
                    "\t:WL-Proxy-Client-IP:" +
                    request.getHeader("WL-Proxy-Client-IP") + "\tRemoteAddr:" + request
                    .getRemoteAddr());
        }
        String ip;
        @SuppressWarnings("unchecked")
        Enumeration<String> xffs = request.getHeaders("X-Forwarded-For");
        if (xffs.hasMoreElements()) {
            String xff = xffs.nextElement();
            ip = resolveClientIPFromXFF(xff);
            if (isValidIP(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIP(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIP(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 从X-Forwarded-For头部中获取客户端的真实IP。 X-Forwarded-For并不是RFC定义的标准HTTP请求Header
     * ，可以参考http://en.wikipedia.org/wiki/X-Forwarded-For
     *
     * @param xff X-Forwarded-For头部的值
     * @return 如果能够解析到client IP，则返回表示该IP的字符串，否则返回null
     */
    private String resolveClientIPFromXFF(String xff) {
        if (xff == null || xff.isEmpty()) {
            return null;
        }
        String[] ss = xff.split(",");
        for (int i = ss.length - 1; i >= 0; i--) {// x-forward-for链反向遍历
            String ip = ss[i].trim();
            if (isValidIP(ip)) {
                return ip;
            }
        }

        return null;
    }

    private static final Pattern ipPattern = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");

    private boolean isValidIP(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return ipPattern.matcher(ip).matches();
    }

    private Map<String, String> getRequestParamValueMap(HttpServletRequest request) {
        Map<String, String> param2value = new HashMap<>();
        Enumeration e = request.getParameterNames();
        String param;

        while (e.hasMoreElements()) {
            param = (String) e.nextElement();
            if (param != null) {
                String value = request.getParameter(param);
                if (value != null) {
                    param2value.put(param, value);
                }
            }
        }

        return param2value;
    }
}
