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
@TableName("financial_entry")
public class FinancialEntryDO extends BaseDO {

    private String ledgerId;

    private LocalDate date;

    private Long amount;

    private String remark;

    public void setLedger_id(String ledgerId) {
        this.ledgerId = ledgerId;
    }


}