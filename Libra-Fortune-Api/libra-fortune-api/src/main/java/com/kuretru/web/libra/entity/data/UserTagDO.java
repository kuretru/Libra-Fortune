package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entry")
public class LedgerEntryDO extends BaseDO {

    private String ledgerId;

    private String categoryId;

    private LocalDate date;

    private Long amount;

    private String remark;

    public void setLedger_id(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public void setCategory_id(String categoryId) {
        this.categoryId = categoryId;
    }

}