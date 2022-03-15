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
@TableName("co_ledger_entry")
public class CoLedgerEntryDO extends BaseDO {

    private String entryId;

    private String userId;

    private Long amount;

    public void setEntry_id(String ledgerId) {
        this.entryId = entryId;
    }

    public void setUserId_id(String categoryId) {
        this.userId = userId;
    }

}