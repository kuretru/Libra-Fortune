package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;


@Data
public class LedgerEntryQuery {

    private UUID ledgerId;

    private UUID categoryId;

    //    忘记是什么类型了 之后查一查
    private String date;

    private Long amount;

}