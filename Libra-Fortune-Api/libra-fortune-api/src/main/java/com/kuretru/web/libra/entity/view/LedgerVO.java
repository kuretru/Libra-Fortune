package com.kuretru.web.libra.entity.view;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerVO extends BaseDTO {

    private String name;

    private LedgerTypeEnum type;

    private String remark;

    private UUID ownerId;

    private String ownerNickname;

    private String ownerAvatar;

}
