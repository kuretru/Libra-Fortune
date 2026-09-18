package com.kuretru.web.libra.account.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseCreateUpdateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("account_transfer")
public class AccountTransferDO extends BaseCreateUpdateDO {

    /** 转账记录Owner */
    private String owner;

    /** 转账日期 */
    private LocalDate date;

    /** 转账名称 */
    private String name;

    /** 转出账户ID */
    private Long sourceAccountId;

    /** 转出金额 */
    private BigDecimal sourceAmount;

    /** 转出货币 */
    private String sourceCurrency;

    /** 转入账户ID */
    private Long targetAccountId;

    /** 转入金额 */
    private BigDecimal targetAmount;

    /** 转入货币 */
    private String targetCurrency;

    /** 备注 */
    private String remark;

}
