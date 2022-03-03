package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerPermissionDTO extends BaseDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID ledgerId;

    @NotNull
    private Boolean writable;

    @NotNull
    private Boolean readable;

}
