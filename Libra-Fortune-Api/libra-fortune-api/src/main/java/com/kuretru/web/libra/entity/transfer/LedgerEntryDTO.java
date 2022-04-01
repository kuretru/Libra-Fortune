package com.kuretru.web.libra.entity.transfer;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDTO extends BaseDTO {

    @NotNull
    private UUID ledgerId;

    @NotNull
    private UUID categoryId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long amount;

    @NotNull
    @Size(max = 64)
    private String remark;

}
