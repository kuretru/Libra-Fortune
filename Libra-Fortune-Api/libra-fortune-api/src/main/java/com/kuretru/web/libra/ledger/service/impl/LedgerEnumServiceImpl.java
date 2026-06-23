package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.common.utils.EnumUtils;
import com.kuretru.web.libra.ledger.entity.enums.DetailLockType;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEnumDTO;
import com.kuretru.web.libra.ledger.service.LedgerEnumService;
import org.springframework.stereotype.Service;

@Service
public class LedgerEnumServiceImpl implements LedgerEnumService {

    @Override
    public LedgerEnumDTO enums() {
        var result = new LedgerEnumDTO();
        result.setDetailLockTypes(EnumUtils.buildDTO(DetailLockType.values()));
        return result;
    }

}

