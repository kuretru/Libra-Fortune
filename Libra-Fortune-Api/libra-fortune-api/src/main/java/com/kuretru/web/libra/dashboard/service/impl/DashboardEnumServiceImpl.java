package com.kuretru.web.libra.dashboard.service.impl;

import com.kuretru.microservices.common.utils.EnumUtils;
import com.kuretru.microservices.dashboard.entity.enums.FilterLogic;
import com.kuretru.microservices.dashboard.entity.enums.FilterOperator;
import com.kuretru.microservices.dashboard.entity.enums.OrderByType;
import com.kuretru.microservices.web.entity.enums.SortOrderEnum;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerDimensions;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerMetrics;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerTimeDimension;
import com.kuretru.web.libra.dashboard.entity.transfer.DashboardEnumDTO;
import com.kuretru.web.libra.dashboard.service.DashboardEnumService;
import org.springframework.stereotype.Service;

@Service
public class DashboardEnumServiceImpl implements DashboardEnumService {

    @Override
    public DashboardEnumDTO enums() {
        var result = new DashboardEnumDTO();
        result.setTimeDimensions(EnumUtils.buildDTO(LedgerTimeDimension.values()));
        result.setMetrics(EnumUtils.buildDTO(LedgerMetrics.values()));
        result.setDimensions(EnumUtils.buildDTO(LedgerDimensions.values()));
        result.setFilterLogics(EnumUtils.buildDTO(FilterLogic.values()));
        result.setFilterOperators(EnumUtils.buildDTO(FilterOperator.values()));
        result.setOrderByTypes(EnumUtils.buildDTO(OrderByType.values()));
        result.setOrderByModes(EnumUtils.buildDTO(SortOrderEnum.values()));
        return result;
    }

}
