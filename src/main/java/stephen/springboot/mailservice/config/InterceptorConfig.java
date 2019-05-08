package stephen.springboot.mailservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import stephen.springboot.mailservice.interceptor.IpInterceptor;
import stephen.springboot.mailservice.interceptor.LogInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器配置
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mailservice.whitelist")
public class InterceptorConfig implements WebMvcConfigurer {
    private List<String> ip = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()); // 日志拦截器
        registry.addInterceptor(new IpInterceptor(ip));  // IP白名单拦截器
    }
}
