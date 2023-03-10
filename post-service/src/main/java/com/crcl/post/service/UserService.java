package com.crcl.post.service;


import com.crcl.post.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto getCurrentUser();

    List<UserDto> getUsersByUserName(Set<String> usersNames);
}
