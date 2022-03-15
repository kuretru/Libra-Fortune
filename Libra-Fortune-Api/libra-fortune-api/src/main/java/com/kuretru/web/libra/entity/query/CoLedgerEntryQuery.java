package com.kuretru.web.libra.entity.query;

import lombok.Data;

import java.util.UUID;


@Data
public class CoLedgerEntryQuery {
    private UUID entryId;

    private UUID userId;

    private Long amount;
}