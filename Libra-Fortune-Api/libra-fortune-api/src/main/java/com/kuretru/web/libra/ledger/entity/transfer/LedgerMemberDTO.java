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
public class LedgerMemberDTO extends BaseCreateUpdateDTO {

    @JsonIgnore
    @Schema(description = "账本ID")
    private Long ledgerId;

    @Schema(description = "成员用户名")
    private String username;

    @Schema(description = "默认承担比例")
    private BigDecimal defaultFundedRatio;

}
