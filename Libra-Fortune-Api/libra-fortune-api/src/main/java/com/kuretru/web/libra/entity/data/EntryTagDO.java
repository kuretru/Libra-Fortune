package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("entry_tag")
public class EntryTagDO extends BaseDO {

    private String entryId;

    private String tagId;

    public void setEntry_id(String entryId) {
        this.entryId = entryId;
    }

    public void setTag_id(String tagId) {
        this.tagId = tagId;
    }

}