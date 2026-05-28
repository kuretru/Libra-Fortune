package com.kuretru.web.libra.metadata.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MetadataTagSetItemQuery {

    @Schema(description = "关联标签组ID")
    private Long setId;

    @Schema(description = "多选，关联标签组ID")
    private List<Long> setIdIn;

}
