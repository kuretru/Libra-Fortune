package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;


@Data
public class CoLedgerUserQuery {

    private UUID ledgerId;

    private UUID userId;

    private Boolean isWritable;

}