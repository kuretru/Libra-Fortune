package com.kuretru.web.libra.metadata.entity.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetadataCategoryDTO extends BaseCreateUpdateDTO {

    @Schema(description = "父分类ID")
    private Long parentId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "子分类", accessMode = Schema.AccessMode.READ_ONLY)
    private List<MetadataCategoryDTO> children;

}
