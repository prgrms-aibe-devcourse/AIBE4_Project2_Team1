package websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.path-student}")
    private String studentPath;

    @Value("${file.path-teacher}")
    private String teacherPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹에서 /studentImage/** 로 접속하면 실제 로컬의 studentPath 폴더를 뒤짐
        registry.addResourceHandler("/studentImage/**", "/teacherImage/**")
                .addResourceLocations("file:" + studentPath, "file:" + teacherPath);
    }
}