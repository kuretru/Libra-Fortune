package com.kuretru.web.libra.ledger.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerDTO extends BaseCreateUpdateDTO {

    @Schema(description = "账本Owner")
    private String owner;

    @NotEmpty
    @Size(min = 1, max = 16)
    @Schema(description = "账本名称")
    private String name;

    @Schema(description = "账本承担成员")
    private List<LedgerMemberDTO> members;

}
