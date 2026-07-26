package com.kuretru.web.libra.dashboard.mapper;

import com.kuretru.microservices.dashboard.mapper.DashboardSqlProvider;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DashboardMapper {

    @SelectProvider(type = DashboardSqlProvider.class, method = "buildQuery")
    List<DashboardLedgerBO> query(DashboardLedgerQuery query);

}
