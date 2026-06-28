package com.kuretru.web.libra.metadata.entity.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuretru.microservices.web.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotEmpty
    @Size(min = 1, max = 32)
    @Schema(description = "分类名称")
    private String name;

    @Size(max = 32)
    @Schema(description = "图标")
    private String icon;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "子分类", accessMode = Schema.AccessMode.READ_ONLY)
    private List<MetadataCategoryDTO> children;

}
