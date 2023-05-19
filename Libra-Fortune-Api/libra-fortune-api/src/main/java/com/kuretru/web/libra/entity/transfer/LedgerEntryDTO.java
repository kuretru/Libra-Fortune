package com.kuretru.web.libra.entity.transfer;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
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

    @NotEmpty
    @Size(max = 16)
    private String name;

    @NotNull
    private Long total;

    @NotEmpty
    @Size(min = 3, max = 3)
    private String currencyType;

    @NotNull
    @Size(max = 64)
    private String remark;

    @NotEmpty
    private List<LedgerEntryDetailDTO> details;

}
