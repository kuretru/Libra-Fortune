package com.kuretru.web.libra.dashboard.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DashboardMapper {

    List<DashboardLedgerBO> sum(@Param(Constants.WRAPPER) QueryWrapper<DashboardLedgerBO> queryWrapper,
                                @Param("sum") String sum,
                                @Param("selectColumns") List<String> selectColumns,
                                @Param("groupByColumns") List<String> groupByColumns,
                                @Param("joinDetail") boolean joinDetail,
                                @Param("joinTag") boolean joinTag);

}
