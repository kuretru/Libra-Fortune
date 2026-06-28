package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.kuretru.microservices.web.entity.annotations.ChildrenParentId;
import com.kuretru.microservices.web.entity.interfaces.Children;
import com.kuretru.microservices.web.entity.data.BaseCreateUpdateDO;
import com.kuretru.web.libra.ledger.entity.enums.DetailLockType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "ledger_v2_entry_detail", autoResultMap = true)
public class LedgerEntryDetailDO extends BaseCreateUpdateDO implements Children<LedgerEntryDetailDO> {

    /** 外键，条目ID */
    @ChildrenParentId
    private Long entryId;

    /** 归属用户名 */
    private String username;

    /** 百分比或金额锁定方式 */
    private DetailLockType lockType;

    /** 付款链，按顺序存储账户ID */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> paymentChain;

    /** 百分比，承担比例 */
    private BigDecimal fundedRatio;

    /** 承担金额 */
    private BigDecimal amount;

    @Override
    public boolean bizEqual(LedgerEntryDetailDO newRecord) {
        return Objects.equals(username, newRecord.getUsername())
                && Objects.equals(lockType, newRecord.getLockType())
                && Objects.equals(paymentChain, newRecord.getPaymentChain())
                && Objects.equals(fundedRatio, newRecord.getFundedRatio())
                && Objects.equals(amount, newRecord.getAmount());
    }

}
