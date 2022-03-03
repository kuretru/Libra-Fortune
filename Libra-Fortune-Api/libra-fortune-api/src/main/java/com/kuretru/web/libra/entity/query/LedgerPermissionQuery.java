package com.kuretru.web.libra.entity.query;

import lombok.Data;

import java.util.UUID;


@Data
public class LedgerPermissionQuery {

    private UUID userId;

    private UUID ledgerId;

}
