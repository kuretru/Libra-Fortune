package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseRelationDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本条目标签关联表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entry_tag")
public class LedgerEntryTagDO extends BaseRelationDO {

    /** 外键，账本条目ID */
    private String entryId;

    /** 外键，账本标签ID */
    private String tagId;

}
