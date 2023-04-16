package com.kuretru.web.libra.entity.transfer;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerCategoryDTO extends BaseDTO {

    @NotNull
    private UUID ledgerId;

    @NotEmpty
    private String name;

}
