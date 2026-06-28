package com.kuretru.web.libra.account.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.interfaces.Sequenced;
import com.kuretru.microservices.web.entity.data.BaseCreateUpdateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("account")
public class AccountDO extends BaseCreateUpdateDO implements Sequenced {

    /** 账户Owner */
    private String owner;

    /** 账户名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 是否可以储蓄 */
    private Boolean canHoldFunds;

    /** 排序标识 */
    private Integer sequence;

}
