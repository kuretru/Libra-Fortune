package com.kuretru.web.libra.ledger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.web.libra.ledger.entity.business.DashboardBO;
import com.kuretru.web.libra.ledger.entity.query.DashboardQuery;
import com.kuretru.web.libra.ledger.mapper.DashboardMapper;
import com.kuretru.web.libra.ledger.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper mapper;

    @Autowired
    public DashboardServiceImpl(DashboardMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<DashboardBO> sum(DashboardQuery query) {
        String sum = query.getSumMode().getSelect();

        var selectColumns = new ArrayList<String>();
        var groupByColumns = new ArrayList<String>();
        for (var groupBy : query.getGroupBy()) {
            selectColumns.add(groupBy.getSelect());
            groupByColumns.add(groupBy.getGroupBy());
        }

        var queryWrapper = new QueryWrapper<DashboardBO>();
        queryWrapper.ge("entry.`date`", query.getDateBegin());
        queryWrapper.le("entry.`date`", query.getDateEnd());
        var filter = query.getFilter();
        if (filter != null && filter.getLedgerId() != null && !filter.getLedgerId().isEmpty()) {
            queryWrapper.in("entry.ledger_id", filter.getLedgerId());
        }

        return mapper.sum(queryWrapper, sum, selectColumns, groupByColumns);
    }

}
