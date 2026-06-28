package com.kuretru.web.libra.metadata.entity.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuretru.microservices.web.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetadataTagSetDTO extends BaseCreateUpdateDTO {

    @NotEmpty
    @Size(min = 1, max = 16)
    @Schema(description = "标签组名称")
    private String name;

    @NotNull
    @Schema(description = "该标签组是否必选")
    private Boolean required;

    @NotNull
    @Schema(description = "是否允许多选")
    private Boolean allowMultiple;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "标签", accessMode = Schema.AccessMode.READ_ONLY)
    private List<MetadataTagSetItemDTO> items;

}
