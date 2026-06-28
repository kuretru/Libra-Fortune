package com.kuretru.web.libra.metadata.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MetadataTagSetQuery {

    @Schema(description = "模糊搜索，标签组名称")
    private String nameLike;

    @Schema(description = "精准搜索，该标签组是否必须选择一个标签")
    private Boolean required;

    @Schema(description = "精准搜索，是否允许多选")
    private Boolean allowMultiple;

    @Schema(description = "模糊搜索，标签名称")
    private String tagNameLike;

}
