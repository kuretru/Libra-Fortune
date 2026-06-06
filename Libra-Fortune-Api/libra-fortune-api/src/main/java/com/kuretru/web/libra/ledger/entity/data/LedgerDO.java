package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.interfaces.Sequenced;
import com.kuretru.microservices.web.v2.entity.data.BaseCreateUpdateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2")
public class LedgerDO extends BaseCreateUpdateDO implements Sequenced {

    /** 账本Owner */
    private String owner;

    /** 账本名称 */
    private String name;

    /** 排序标识 */
    private Integer sequence;

}
