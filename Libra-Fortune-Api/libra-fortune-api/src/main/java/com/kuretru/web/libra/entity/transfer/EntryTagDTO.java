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
public class EntryTagDTO extends BaseDTO {

    @NotNull
    private UUID entryId;

    @NotNull
    private UUID tagId;

}