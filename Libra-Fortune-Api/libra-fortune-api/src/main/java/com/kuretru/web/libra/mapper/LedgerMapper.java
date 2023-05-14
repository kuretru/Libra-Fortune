package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kuretru.web.libra.entity.business.LedgerBO;
import com.kuretru.web.libra.entity.data.LedgerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Repository
@Mapper
public interface LedgerMapper extends BaseMapper<LedgerDO> {

    /**
     * 分页查询BO
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return BO
     */
    IPage<LedgerBO> listPageBo(IPage<LedgerBO> page, @Param(Constants.WRAPPER) QueryWrapper<LedgerBO> queryWrapper);

}
