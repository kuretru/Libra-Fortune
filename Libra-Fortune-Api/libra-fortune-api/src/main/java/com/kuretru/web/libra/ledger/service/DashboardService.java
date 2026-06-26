package com.kuretru.web.libra.ledger.service;

import com.kuretru.web.libra.ledger.entity.business.DashboardBO;
import com.kuretru.web.libra.ledger.entity.query.DashboardQuery;

import java.util.List;

public interface DashboardService {

    List<DashboardBO> sum(DashboardQuery query);

}
