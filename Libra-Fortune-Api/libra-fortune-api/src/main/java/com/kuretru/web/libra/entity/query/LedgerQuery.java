package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LedgerQuery {

    @Size(max = 32)
    private String name;

}
