package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("co_ledger_user")
public class CoLedgerUserDO extends BaseDO {

    private String ledgerId;

    private String userId;

    @TableField("is_writable")
    private Boolean isWritable;

    public void setLedger_id(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public void setUser_id(String userId) {
        this.userId = userId;
    }
}