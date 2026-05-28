package com.kuretru.web.libra.account.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.v2.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("account_balance")
public class AccountBalanceDO extends BaseDO {

    /** 账户ID */
    private Long accountId;

    /** 日期，表达截止当前的余额 */
    private LocalDate date;

    /** 余额 */
    private BigDecimal balance;

}
