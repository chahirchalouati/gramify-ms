package com.crcl.profile.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;


public class SrvConfiguration {

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(Oauth2TokenInterceptorHelper helper) {
        return new ServerRequestInterceptor(helper);
    }

}
