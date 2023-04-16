package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerTagQuery {

    @NotNull
    private UUID userId;

    private String name;

}
