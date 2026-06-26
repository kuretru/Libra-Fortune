package com.kuretru.web.libra.ledger.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kuretru.web.libra.ledger.entity.business.DashboardBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DashboardMapper {

    List<DashboardBO> sum(@Param(Constants.WRAPPER) QueryWrapper<DashboardBO> queryWrapper,
                          @Param("sum") String sum,
                          @Param("selectColumns") List<String> selectColumns,
                          @Param("groupByColumns") List<String> groupByColumns);

}
