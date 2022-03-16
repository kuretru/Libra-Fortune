package com.kuretru.web.libra.entity.query;

import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;


@Data
public class LedgerQuery {

    @Size(max = 16)
    private String name;

    private UUID ownerId;

    private LedgerTypeEnum type;
}