package com.kuretru.web.libra.ledger.entity.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import com.kuretru.web.libra.ledger.entity.enums.DetailLockType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDetailDTO extends BaseCreateUpdateDTO {

    @JsonIgnore
    @Schema(description = "条目ID")
    private Long entryId;

    @NotEmpty
    @Size(min = 1, max = 32)
    @Schema(description = "分担用户名，可非账本成员")
    private String username;

    @NotNull
    @Schema(description = "百分比或金额锁定方式")
    private DetailLockType lockType;

    @Schema(description = "付款链，按顺序存储账户ID")
    private List<Long> paymentChain;

    @NotNull
    @Schema(description = "定点数，承担比例")
    private BigDecimal fundedRatio;

    @NotNull
    @Schema(description = "定点数，承担金额")
    private BigDecimal amount;

}
