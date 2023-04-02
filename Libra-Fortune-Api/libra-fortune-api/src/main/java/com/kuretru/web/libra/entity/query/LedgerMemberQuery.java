package com.kuretru.web.libra.entity.query;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerMemberQuery {

    @NotNull
    private UUID ledgerId;

}
