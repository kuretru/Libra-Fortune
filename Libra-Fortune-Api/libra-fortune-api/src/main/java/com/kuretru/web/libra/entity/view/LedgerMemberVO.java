package com.kuretru.web.libra.entity.view;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
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
public class LedgerMemberVO extends BaseDTO {

    private UUID userId;

    private String userNickname;

    private String userAvatar;

}
