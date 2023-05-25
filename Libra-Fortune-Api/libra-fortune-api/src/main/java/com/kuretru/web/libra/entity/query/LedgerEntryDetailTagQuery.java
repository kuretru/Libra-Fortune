package com.kuretru.web.libra.entity.query;

import lombok.Data;

import java.util.UUID;


/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerEntryDetailTagQuery {

    private UUID entryDetailId;

    private UUID tagId;

}
