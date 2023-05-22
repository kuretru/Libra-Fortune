package com.kuretru.web.libra.entity.view;

import com.kuretru.microservices.web.entity.transfer.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDetailVO extends BaseDTO {

    private Short fundedRatio;

    private Long amount;

    private UserVO user;

    private PaymentChannelVO paymentChannel;

    private List<LedgerTagVO> tags;

}
