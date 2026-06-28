package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.annotations.ChildrenParentId;
import com.kuretru.microservices.web.entity.interfaces.Children;
import com.kuretru.microservices.web.entity.data.BaseCreateUpdateDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_member")
public class LedgerMemberDO extends BaseCreateUpdateDO implements Children<LedgerMemberDO> {

    /** 外键，账本ID */
    @ChildrenParentId
    private Long ledgerId;

    /** 成员用户名 */
    private String username;

    /** 百分数，默认承担比例 */
    private BigDecimal defaultFundedRatio;

    @Override
    public boolean bizEqual(LedgerMemberDO newRecord) {
        return Objects.equals(username, newRecord.getUsername())
                && Objects.equals(defaultFundedRatio, newRecord.getDefaultFundedRatio());
    }

}
