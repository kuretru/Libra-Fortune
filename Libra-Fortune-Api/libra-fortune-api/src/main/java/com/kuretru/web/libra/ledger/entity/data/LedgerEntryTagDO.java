package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.annotations.ChildrenParentId;
import com.kuretru.microservices.web.entity.interfaces.Children;
import com.kuretru.microservices.web.entity.data.BaseCreateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_entry_tag")
public class LedgerEntryTagDO extends BaseCreateDO implements Children<LedgerEntryTagDO> {

    /** 外键，条目ID */
    @ChildrenParentId
    private Long entryId;

    /** 外间，标签ID */
    private Long tagId;

    @Override
    public boolean bizEqual(LedgerEntryTagDO newRecord) {
        return Objects.equals(tagId, newRecord.getTagId());
    }

}
