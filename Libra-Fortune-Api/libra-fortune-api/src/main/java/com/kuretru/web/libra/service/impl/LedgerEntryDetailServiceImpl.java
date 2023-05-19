package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
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

    public List<LedgerEntryDetailDTO> listByEntryId(UUID entryId) {
        QueryWrapper<LedgerEntryDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", entryId.toString());
        queryWrapper.orderByAsc("id");
        List<LedgerEntryDetailDTO> result = list(queryWrapper);
        result.forEach(record -> {
            if (record.getUserId().equals(AccessTokenContext.getUserId())) {
                record.setTags(entryTagService.listByEntryDetailId(record.getId()));
            }
        });
        return result;
    }

    @Override
    public List<LedgerEntryDetailDTO> save(UUID entryId, List<LedgerEntryDetailDTO> records) throws ServiceException {
        records.forEach(record -> {
            LedgerEntryDetailDTO newRecord = super.save(record);

            if (record.getUserId().equals(AccessTokenContext.getUserId())) {
                record.getTags().forEach(tag -> tag.setEntryDetailId(newRecord.getId()));
                entryTagService.save(record.getTags());
            }
        });

        return listByEntryId(entryId);
    }

    @Override
    public List<LedgerEntryDetailDTO> update(UUID entryId, List<LedgerEntryDetailDTO> records) throws ServiceException {
        QueryWrapper<LedgerEntryDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", records.get(0).toString());
        List<LedgerEntryDetailDO> oldRecords = mapper.selectList(queryWrapper);
        Map<UUID, LedgerEntryDetailDO> oldRecordsMap = new HashMap<>(HashMapUtils.initialCapacity(oldRecords.size()));
        oldRecords.forEach(detail -> oldRecordsMap.put(UUID.fromString(detail.getUserId()), detail));

        List<LedgerEntryDetailDTO> result = new ArrayList<>();
        for (LedgerEntryDetailDTO record : records) {
            if (!oldRecordsMap.containsKey(record.getUserId())) {
                // 没有这条记录新增
                result.add(super.save(record));
            } else {
                // 有则更新
                result.add(super.update(record));
                oldRecordsMap.remove(record.getUserId());
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

    LedgerEntryDetailDTO getMyEntryDetail(List<LedgerEntryDetailDTO> records) {
        for (LedgerEntryDetailDTO record : records) {
            if (record.getUserId().equals(AccessTokenContext.getUserId())) {
                return record;
            }
        }
        return null;
    }

}
