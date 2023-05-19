package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.common.utils.HashMapUtils;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.mapper.LedgerTagEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerTagMapper;
import com.kuretru.web.libra.service.LedgerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerTagServiceImpl
        extends BaseServiceImpl<LedgerTagMapper, LedgerTagDO, LedgerTagDTO, LedgerTagQuery>
        implements LedgerTagService {

    @Autowired
    public LedgerTagServiceImpl(LedgerTagMapper mapper, LedgerTagEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public Map<UUID, LedgerTagDTO> listMyLedgerTags() {
        List<LedgerTagDTO> records = list();
        Map<UUID, LedgerTagDTO> result = new HashMap<>(HashMapUtils.initialCapacity(records.size()));
        records.forEach(record -> result.put(record.getId(), record));
        return result;
    }

    @Override
    public List<LedgerTagDTO> list() {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "账本标签必须关联用户ID查询");
    }

    @Override
    public PaginationResponse<LedgerTagDTO> list(PaginationQuery paginationQuery) {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "账本标签必须关联用户ID查询");
    }

    @Override
    public LedgerTagDTO save(LedgerTagDTO record) throws ServiceException {
        record.setUserId(AccessTokenContext.getUserId());
        return super.save(record);
    }

    @Override
    public LedgerTagDTO update(LedgerTagDTO record) throws ServiceException {
        record.setUserId(AccessTokenContext.getUserId());

        LedgerTagDO old = getDO(record.getId());
        if (old == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定记录不存在");
        }

        // 不能把别人的记录修改为自己的
        boolean notMyRecord = !UUID.fromString(old.getUserId()).equals(record.getUserId());
        if (notMyRecord) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无法修改非自己的账本标签");
        }

        return super.update(record);
    }

    @Override
    protected QueryWrapper<LedgerTagDO> buildQueryWrapper(LedgerTagQuery query) {
        QueryWrapper<LedgerTagDO> queryWrapper = super.buildQueryWrapper(query);
        queryWrapper.eq("user_id", AccessTokenContext.getUserId().toString());
        return queryWrapper;
    }

    @Override
    protected void verifyCanGet(LedgerTagDO record) throws ServiceException {
        boolean notMe = !UUID.fromString(record.getUserId()).equals(AccessTokenContext.getUserId());
        if (notMe) {
            throw new ServiceException(UserErrorCodes.ACCESS_UNAUTHORIZED, "只能查询自己的账本标签");
        }
    }

    @Override
    protected void verifyCanRemove(LedgerTagDO record) throws ServiceException {
        throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "尚未实现");
    }

    @Override
    protected LedgerTagDTO findUniqueRecord(LedgerTagDTO record) {
        QueryWrapper<LedgerTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", record.getUserId());
        queryWrapper.eq("name", record.getName());
        LedgerTagDO result = mapper.selectOne(queryWrapper);
        return entityMapper.doToDto(result);
    }

}
