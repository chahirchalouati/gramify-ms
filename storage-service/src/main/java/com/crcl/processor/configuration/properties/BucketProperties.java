package com.crcl.processor.configuration.properties;

import lombok.Data;

@Data
public class BucketProperties {
    private boolean objectLock = false;
    private boolean isDefault = false;
    private String name;
}
