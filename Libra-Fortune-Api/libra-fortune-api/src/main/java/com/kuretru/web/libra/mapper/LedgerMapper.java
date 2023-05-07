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
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<LedgerBO> listPageBo(IPage<LedgerBO> page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
