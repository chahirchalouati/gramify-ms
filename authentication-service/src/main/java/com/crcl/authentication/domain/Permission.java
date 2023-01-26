package com.crcl.authentication.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document("permissions")
@Data
public class Permission {
    @Id
    private String id;
    @NotBlank
    private String name;
    private boolean enabled = true;

    public Permission(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}
