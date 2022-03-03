package com.kuretru.web.libra.entity.query;

import lombok.Data;

import java.util.UUID;


@Data
public class LedgerEntityTagQuery {
    private UUID entityId;

    private UUID tagId;


    public void setTag_id(UUID tagId) {
        this.tagId = tagId;
    }

    public void setEntity_id(UUID entityId) {
        this.entityId = entityId;
    }

}
