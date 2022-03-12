package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_category")
public class LedgerCategoryDO extends BaseDO {

    private String ledgerId;

    private String name;

    public void setLedger_id(String ledgerId) {
        this.ledgerId = ledgerId;
    }

}