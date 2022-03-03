package com.kuretru.web.libra.entity.query;

import lombok.Data;

import java.util.UUID;


@Data
public class LedgerEntityQuery {
    private UUID ledgerId;

    private UUID categoryId;

    private String date;

    private Long amount;

    public void setLedger_id(UUID ledgerId) {
        this.ledgerId = ledgerId;
    }

    public void setCategory_id(UUID categoryId) {
        this.categoryId = categoryId;
    }

}
