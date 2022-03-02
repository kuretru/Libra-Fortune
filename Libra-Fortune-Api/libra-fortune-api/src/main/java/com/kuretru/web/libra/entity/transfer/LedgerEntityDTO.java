package com.kuretru.web.libra.entity.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuretru.api.common.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;



@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "账目-数据传输实体")
public class LedgerEntityDTO extends BaseDTO {
    @NotNull
    @Size(max = 64)
    @Schema(description = "账本ID")
    private String ledgerId;

    @NotNull
    @Size(max = 64)
    @Schema(description = "大分类ID")
    private String categoryId;

    @NotNull
    @Schema(description = "发生时间")
    private String date;

    @NotNull
    @Schema(description = "费用")
    private Long amount;

    @Size(max = 64)
    @Schema(description = "备注")
    private String remark;

}
