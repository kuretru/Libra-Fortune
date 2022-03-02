package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "账本-数据传输实体")
public class LedgerDTO extends BaseDTO {

    @NotNull
    @Schema(description = "所属用户ID")
    private String userId;

    @NotNull
    @Size(max = 16)
    @Schema(description = "账本名称")
    private String name;

    @Size(max = 64)
    @Schema(description = "账本备注")
    private String remark;

}
