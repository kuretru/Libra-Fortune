package com.kuretru.web.libra.entity.query;

import lombok.Data;

import java.util.UUID;


@Data
public class EntryTagQuery {

    private UUID entryId;

    private UUID tagId;
}