package com.crcl.audit.queue;

import com.crcl.common.dto.queue.events.QEvent;
import com.crcl.common.dto.requests.AuditRequest;

public interface AuditQueueProcessor {

    void process(QEvent<AuditRequest> event);
}
