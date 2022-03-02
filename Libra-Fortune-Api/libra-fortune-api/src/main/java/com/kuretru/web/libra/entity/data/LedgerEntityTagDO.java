package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账目与标签的关联
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entity_tag")
public class LedgerEntityTagDO extends BaseDO {
    /** 账目 */
    private String entityId;
    /** 标签 */
    private String tagId;

}
