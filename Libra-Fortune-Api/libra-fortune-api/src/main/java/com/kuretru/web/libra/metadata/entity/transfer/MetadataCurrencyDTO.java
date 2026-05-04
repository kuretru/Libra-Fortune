package com.kuretru.web.libra.metadata.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetadataCurrencyDTO extends BaseDTO {

    @Schema(description = "货币代码")
    private String code;

    @Schema(description = "货币符号")
    private String symbol;

    @Schema(description = "货币名称")
    private String name;

}
