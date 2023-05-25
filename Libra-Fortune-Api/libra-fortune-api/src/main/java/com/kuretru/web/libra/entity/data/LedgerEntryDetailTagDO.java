package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本条目明细标签关联表
 * 多对多关系
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entry_detail_tag")
public class LedgerEntryDetailTagDO extends BaseDO {

    /** 外键，账本条目明细ID */
    private String entryDetailId;

    /** 外键，账本标签ID */
    private String tagId;

}
