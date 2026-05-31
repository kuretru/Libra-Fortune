package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.v2.entity.data.BaseCreateOnlyDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_entry_tag")
public class LedgerEntryTagDO extends BaseCreateOnlyDO {

    /** 外键，条目ID */
    private Long entryId;

    /** 标签ID */
    private Long tagId;

}
