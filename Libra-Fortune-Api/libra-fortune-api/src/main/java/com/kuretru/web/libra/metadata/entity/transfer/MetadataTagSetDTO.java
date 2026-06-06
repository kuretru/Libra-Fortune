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
public class MetadataTagSetDTO extends BaseCreateUpdateDTO {

    @Schema(description = "标签组名称")
    private String name;

    @Schema(description = "该标签组是否必选")
    private Boolean required;

    @Schema(description = "是否允许多选")
    private Boolean allowMultiple;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "标签", accessMode = Schema.AccessMode.READ_ONLY)
    private List<MetadataTagSetItemDTO> items;

}
