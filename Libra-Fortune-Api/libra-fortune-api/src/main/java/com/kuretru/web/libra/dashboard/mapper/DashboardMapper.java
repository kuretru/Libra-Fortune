package com.kuretru.web.libra.dashboard.mapper;

import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.query.DashboardQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DashboardMapper {

    @SelectProvider(type = DashboardLedgerSqlProvider.class, method = "buildQuery")
    List<DashboardLedgerBO> query(DashboardQuery query);

}
