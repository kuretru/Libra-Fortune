package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;


@Data
public class LedgerTagQuery {
    private UUID userId;

    @Size(max = 16)
    private String name;

    public void setUser_id(UUID userId) {
        this.userId = userId;
    }

}
