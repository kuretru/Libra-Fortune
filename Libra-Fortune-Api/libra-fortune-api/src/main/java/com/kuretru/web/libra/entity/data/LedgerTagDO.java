package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本标签表
 * 标签归属于用户
 * 每个用户只能看到自己的标签
 * 每个条目都可以单独设置标签
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_tag")
public class LedgerTagDO extends BaseDO {

    /** 外键，所属用户ID */
    private String userId;

    /** 标签名称 */
    private String name;

}
