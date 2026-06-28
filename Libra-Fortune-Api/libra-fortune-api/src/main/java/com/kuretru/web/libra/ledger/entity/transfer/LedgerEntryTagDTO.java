package com.kuretru.web.libra.ledger.entity.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryTagDTO extends BaseCreateDTO {

    @JsonIgnore
    @Schema(description = "条目ID")
    private Long entryId;

    @NotNull
    @Schema(description = "标签ID")
    private Long tagId;

}
