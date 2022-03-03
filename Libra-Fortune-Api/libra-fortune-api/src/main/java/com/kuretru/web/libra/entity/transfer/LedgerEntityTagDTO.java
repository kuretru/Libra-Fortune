package com.kuretru.web.libra.entity.transfer;

import com.kuretru.api.common.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.UUID;


/**
 * 账目与标签的关联
 */


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntityTagDTO extends BaseDTO {
    @NotNull
    private UUID entityId;

    @NotNull
    private UUID tagId;

}
