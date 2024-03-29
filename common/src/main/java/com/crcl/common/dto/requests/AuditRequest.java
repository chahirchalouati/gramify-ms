package com.crcl.common.dto.requests;

import com.crcl.common.utils.AuditAction;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class AuditRequest {
    protected String username;
    private String identifier;
    private AuditAction action;
    private Map<String, Object> details = new LinkedHashMap<>();
}
