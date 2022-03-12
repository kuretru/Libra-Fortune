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
@TableName("ledger_entry")
public class LedgerEntryDO extends BaseDO {

    private String ledgerId;

    private String categoryId;

//    忘记是什么类型了 之后查一查
    private String date;

    private Long amount;

    private String remark;

    public void setLedger_id(String ledgerId) {
        this.ledgerId = ledgerId;
    }
    public void setCategory_id(String categoryId) {
        this.categoryId = categoryId;
    }

}