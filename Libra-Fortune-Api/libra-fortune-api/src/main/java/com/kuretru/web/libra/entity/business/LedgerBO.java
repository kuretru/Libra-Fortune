package com.kuretru.web.libra.entity.business;

import com.kuretru.web.libra.entity.data.LedgerDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerBO extends LedgerDO {

    /** 账本主人昵称 */
    private String ownerNickname;

    /** 账本主人头像 */
    private String ownerAvatar;

}
