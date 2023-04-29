package com.crcl.common.queue;

import com.crcl.common.domain.Orientation;
import com.crcl.common.dto.responses.FileUploadResult;
import com.crcl.common.properties.ImageSize;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageUpload extends Auditable {
    private FileUploadResult response;
    private ImageSize imageSize;
    private String id;
    private Orientation orientation;
}