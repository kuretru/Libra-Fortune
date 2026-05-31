package com.kuretru.web.libra.ledger.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerDTO extends BaseDTO {

    @Schema(description = "账本Owner")
    private String owner;

    @Schema(description = "账本名称")
    private String name;

    @Schema(description = "账本承担成员")
    private List<LedgerMemberDTO> members;

}
