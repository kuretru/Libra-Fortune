package com.kuretru.web.libra.entity.query;

import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerQuery {

    private UUID ownerId;

    @Size(max = 16)
    private String name;

    private LedgerTypeEnum type;

}
