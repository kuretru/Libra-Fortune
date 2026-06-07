package com.kuretru.web.libra.metadata.entity.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetadataTagSetItemDTO extends BaseCreateUpdateDTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "关联标签组ID", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long setId;

    @NotEmpty
    @Size(min = 1, max = 32)
    @Schema(description = "标签名称")
    private String name;

}
