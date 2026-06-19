package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.annotations.ChildrenParentId;
import com.kuretru.microservices.web.entity.interfaces.Children;
import com.kuretru.microservices.web.v2.entity.data.BaseCreateUpdateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_entry_detail")
public class LedgerEntryDetailDO extends BaseCreateUpdateDO implements Children<LedgerEntryDetailDO> {

    /** 外键，条目ID */
    @ChildrenParentId
    private Long entryId;

    /** 归属用户名 */
    private String username;

    /** 外键，账户ID */
    private Long accountId;

    /** 百分比，承担比例 */
    private BigDecimal fundedRatio;

    /** 承担金额 */
    private BigDecimal amount;

    @Override
    public boolean bizEqual(LedgerEntryDetailDO newRecord) {
        return username.equals(newRecord.getUsername()) && accountId.equals(newRecord.getAccountId())
                && fundedRatio.equals(newRecord.getFundedRatio()) && amount.equals(newRecord.getAmount());
    }

}
