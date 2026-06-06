package com.kuretru.web.libra.ledger.entity.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDetailDTO extends BaseCreateUpdateDTO {

    @JsonIgnore
    @Schema(description = "条目ID")
    private Long entryId;

    @Schema(description = "归属用户名")
    private String username;

    @Schema(description = "账户ID")
    private Long accountId;

    @Schema(description = "承担比例")
    private BigDecimal fundedRatio;

    @Schema(description = "承担金额")
    private BigDecimal amount;

}
