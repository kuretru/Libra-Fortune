package com.kuretru.web.libra.metadata.entity.transfer;

import com.kuretru.microservices.web.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MetadataCurrencyDTO extends BaseCreateUpdateDTO {

    @NotEmpty
    @Size(min = 3, max = 3)
    @Schema(description = "货币代码")
    private String code;

    @NotEmpty
    @Size(min = 1, max = 1)
    @Schema(description = "货币符号")
    private String symbol;

    @NotEmpty
    @Size(min = 1, max = 16)
    @Schema(description = "货币名称")
    private String name;

}
