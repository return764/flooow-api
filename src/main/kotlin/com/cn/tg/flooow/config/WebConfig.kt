package com.cn.tg.flooow.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("POST", "GET", "OPTIONS", "PUT", "DELETE")
            .maxAge(10000)
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
