package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kuretru.web.libra.entity.business.LedgerMemberBO;
import com.kuretru.web.libra.entity.data.LedgerMemberDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Repository
@Mapper
public interface LedgerMemberMapper extends BaseMapper<LedgerMemberDO> {

    /**
     * 分页查询BO
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return BO
     */
    IPage<LedgerMemberBO> listPageBo(IPage<LedgerMemberBO> page, @Param(Constants.WRAPPER) QueryWrapper<LedgerMemberBO> queryWrapper);

}
