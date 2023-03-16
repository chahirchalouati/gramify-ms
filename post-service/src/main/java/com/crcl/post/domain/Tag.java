package com.crcl.post.domain;

import com.crcl.common.dto.UserDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
class Tag {
    private String avatar;
    private String bgImage;
    private UserDto user;
}
