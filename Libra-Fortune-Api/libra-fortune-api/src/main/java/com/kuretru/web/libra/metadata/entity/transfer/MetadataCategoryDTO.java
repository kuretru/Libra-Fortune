package com.kuretru.web.libra.metadata.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetadataCategoryDTO extends BaseDTO {

    @Schema(description = "分类名称")
    private String name;

}
