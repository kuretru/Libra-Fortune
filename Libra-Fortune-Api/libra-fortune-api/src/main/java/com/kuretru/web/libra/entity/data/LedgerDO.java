package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger")
public class LedgerDO extends BaseDO {

    /** 外键，账本主人ID */
    private String ownerId;

    /** 账本名称 */
    private String name;

    /** 账本类型 */
    private Short type;

    /** 账本备注 */
    private String remark;

}
