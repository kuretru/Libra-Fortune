package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本的访问修改权限
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_permission")
public class LedgerPermissionDO extends BaseDO {

    /** 哪个用户 */
    private String userId;

    /** 对哪个账本 */
    private String ledgerId;

    /** 可读 */
    @TableField("is_readable")
    private Integer readable;

    /** 可改 */
    @TableField("is_writable")
    private Integer writable;

}
