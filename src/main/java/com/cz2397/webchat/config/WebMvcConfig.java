package com.cz2397.webchat.config;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: cailin
 * @Date: 2022.8.25
 * @Description:  Enable user to visit resources in server. (handler for images in ../resource/static)
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // ../resource/static/dist/img
    private String imgPath = new ApplicationHome(getClass()).getSource().getParentFile().toString()+"/img/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+imgPath);
    }

}
