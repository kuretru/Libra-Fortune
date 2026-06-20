package com.kuretru.web.libra.account.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.annotations.ChildrenParentId;
import com.kuretru.microservices.web.entity.interfaces.Children;
import com.kuretru.microservices.web.v2.entity.data.BaseCreateUpdateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("account_balance")
public class AccountBalanceDO extends BaseCreateUpdateDO implements Children<AccountBalanceDO> {

    /** 账户ID */
    @ChildrenParentId
    private Long accountId;

    /** 日期，表达截止当前的余额 */
    private LocalDate date;

    /** 余额 */
    private BigDecimal balance;

    @Override
    public boolean bizEqual(AccountBalanceDO newRecord) {
        return date.equals(newRecord.getDate()) && balance.equals(newRecord.getBalance());
    }

}
