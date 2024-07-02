package com.ams.config;

import com.ams.utils.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;



/*跨域配置，前后端分离情况必配*/
@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")/*所有的当前站点的请求地址，都支持跨域访问*/
                .allowedOrigins("*")/*所有的外部域都可跨域访问*/
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")/*哪些请求 需要跨域配置*/
                .allowCredentials(true) /*是否支持跨域用户凭证*/
                .maxAge(300)/*超时时长设置为5分钟。 时间单位是秒。*/
                .allowedHeaders("*");/*请求体的头部*/
    }

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("消息转换器启动");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
