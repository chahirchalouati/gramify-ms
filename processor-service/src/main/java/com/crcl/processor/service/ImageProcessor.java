package com.crcl.processor.service;

import com.crcl.common.dto.queue.events.AuthenticatedQEvent;
import com.crcl.common.dto.queue.ImageUpload;

public interface ImageProcessor extends Processor<AuthenticatedQEvent<ImageUpload>> {
}
