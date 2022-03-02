package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


/**
 * 账目的主分类
 */


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "账本许可-数据传输实体")
public class LedgerPermissionDTO extends BaseDTO {

    @NotNull
    @Size(max = 64)
    @Schema(description = "用户ID")
    private String userId;

    @NotNull
    @Size(max = 64)
    @Schema(description = "账本ID")
    private String ledgerId;

    @NotNull
    @Size(max = 1)
    @Schema(description = "可读")
    private Integer readable;

    @NotNull
    @Size(max = 1)
    @Schema(description = "可写")
    private Integer writable;

}
