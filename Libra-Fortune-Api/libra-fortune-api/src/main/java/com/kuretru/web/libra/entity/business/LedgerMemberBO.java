package com.kuretru.web.libra.entity.business;

import com.kuretru.web.libra.entity.data.LedgerMemberDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerMemberBO extends LedgerMemberDO {

    /** 用户昵称 */
    private String nickname;

    /** 用户头像 */
    private String avatar;

}
