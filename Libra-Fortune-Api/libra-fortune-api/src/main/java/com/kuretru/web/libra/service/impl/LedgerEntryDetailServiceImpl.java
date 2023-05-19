package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.common.utils.HashMapUtils;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryDetailDO;
import com.kuretru.web.libra.entity.mapper.LedgerEntryDetailEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerEntryDetailQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailDTO;
import com.kuretru.web.libra.mapper.LedgerEntryDetailMapper;
import com.kuretru.web.libra.service.LedgerEntryDetailService;
import com.kuretru.web.libra.service.LedgerEntryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerEntryDetailServiceImpl
        extends BaseServiceImpl<LedgerEntryDetailMapper, LedgerEntryDetailDO, LedgerEntryDetailDTO, LedgerEntryDetailQuery>
        implements LedgerEntryDetailService {

    private final LedgerEntryTagService entryTagService;

    @Autowired
    public LedgerEntryDetailServiceImpl(LedgerEntryDetailMapper mapper, LedgerEntryDetailEntityMapper entityMapper,
                                        @Lazy LedgerEntryTagService entryTagService) {
        super(mapper, entityMapper);
        this.entryTagService = entryTagService;
    }

    @Override
    public List<LedgerEntryDetailDTO> save(List<LedgerEntryDetailDTO> record) throws ServiceException {
        List<LedgerEntryDetailDTO> result = new ArrayList<>();
        record.forEach(detail -> result.add(super.save(detail)));
        return result;
    }

    @Override
    public List<LedgerEntryDetailDTO> update(List<LedgerEntryDetailDTO> record) throws ServiceException {
        QueryWrapper<LedgerEntryDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", record.get(0).toString());
        List<LedgerEntryDetailDO> oldRecords = mapper.selectList(queryWrapper);
        Map<UUID, LedgerEntryDetailDO> oldRecordsMap = new HashMap<>(HashMapUtils.initialCapacity(oldRecords.size()));
        oldRecords.forEach(detail -> oldRecordsMap.put(UUID.fromString(detail.getUserId()), detail));

        List<LedgerEntryDetailDTO> result = new ArrayList<>();
        for (LedgerEntryDetailDTO detail : record) {
            if (!oldRecordsMap.containsKey(detail.getUserId())) {
                // 没有这条记录新增
                result.add(super.save(detail));
            } else {
                // 有则更新
                result.add(super.update(detail));
                oldRecordsMap.remove(detail.getUserId());
            }
        }
        // 剩下的记录删除
        oldRecordsMap.forEach((userId, detail) -> super.remove(UUID.fromString(detail.getUuid())));
        return result;
    }

    @Override
    public void removeByEntryId(UUID entryId) throws ServiceException {
        QueryWrapper<LedgerEntryDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", entryId.toString());
        List<LedgerEntryDetailDO> records = mapper.selectList(queryWrapper);
        if (records.isEmpty()) {
            throw new ServiceException(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "异常的记录，该条目下没有明细存在");
        }

        mapper.delete(queryWrapper);

        List<UUID> entryDetailIds = records.stream().map(record -> UUID.fromString(record.getUuid())).toList();
        entryTagService.removeByEntryDetailIds(entryDetailIds);
    }

}
