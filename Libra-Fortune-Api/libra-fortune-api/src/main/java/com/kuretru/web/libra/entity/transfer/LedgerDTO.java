package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerDTO extends BaseDTO {

    @NotEmpty
    @Size(max = 16)
    private String name;

    @NotNull
    private UUID ownerId;

    @NotEmpty
    @Size(max = 64)
    private String remark;

    @NotNull
    private LedgerTypeEnum type;

}
