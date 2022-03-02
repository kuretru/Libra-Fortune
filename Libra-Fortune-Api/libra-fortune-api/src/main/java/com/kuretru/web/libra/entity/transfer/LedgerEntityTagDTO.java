package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 账目与标签的关联
 */


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "账目与标签-数据传输实体")
public class LedgerEntityTagDTO extends BaseDTO {
    @NotNull
    @Size(max = 64)
    @Schema(description = "账目ID")
    private String entityId;

    @NotNull
    @Size(max = 64)
    @Schema(description = "标签ID")
    private String tagId;

}
