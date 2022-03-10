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
@TableName("system_user")
public class LedgerDO extends BaseDO {

    private String name;

    private String remark;

    private String ownerId;

    // &8 = 8 -> 合作账本, &1 = 1 普通账本, &2 =2 理财账本
    private Short type;

    public void setOwner_id(String ownerId) {
        this.ownerId = ownerId;
    }

}