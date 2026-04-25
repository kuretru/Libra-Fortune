package com.kuretru.web.libra.entity.query;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerCategoryQuery {

    @NotNull
    private UUID ledgerId;

    @Size(max = 32)
    private String name;

}
