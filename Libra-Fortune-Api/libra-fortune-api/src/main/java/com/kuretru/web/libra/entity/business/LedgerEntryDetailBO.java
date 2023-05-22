package com.kuretru.web.libra.entity.business;

import lombok.Data;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
public class LedgerEntryDetailBO {

    private Long id;

    private String uuid;

    private String userId;

    private String paymentChannelId;

    private Short fundedRatio;

    private Long amount;

    private List<LedgerEntryDetailTagBO> tags;

}
