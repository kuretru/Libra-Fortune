package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
public class LedgerCategoryQuery {

    @Size(max = 16)
    private String name;

    private UUID ledgerId;

}