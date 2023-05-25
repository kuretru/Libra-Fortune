package com.kuretru.web.libra.entity.transfer;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDetailDTO extends BaseDTO {

    private UUID entryId;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID paymentChannelId;

    @NotNull
    private Short fundedRatio;

    @NotNull
    private Long amount;

    private List<UUID> tags;

}
