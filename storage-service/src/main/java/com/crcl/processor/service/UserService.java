package com.crcl.processor.service;

import com.crcl.common.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserService {
    UserDto getCurrentUser();

    Mono<Object> details();

    String getToken();
}
