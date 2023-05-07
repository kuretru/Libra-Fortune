package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerTagQuery {

    @Size(max = 32)
    private String name;

}
