package com.kuretru.web.libra.ledger.entity.transfer;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import lombok.Data;

import java.util.List;

@Data
public class LedgerEnumDTO {

    private List<EnumDTO<String>> entryTypes;

    private List<EnumDTO<String>> detailLockTypes;

}
