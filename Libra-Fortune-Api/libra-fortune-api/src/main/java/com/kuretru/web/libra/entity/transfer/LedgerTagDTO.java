package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "标签-数据传输实体")
public class LedgerTagDTO extends BaseDTO {
    @NotNull
    @Size(max = 64)
    @Schema(description = "用户ID")
    private String userId;

    @NotNull
    @Size(max = 16)
    @Schema(description = "标签名称")
    private String name;

}
