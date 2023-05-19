package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.common.utils.HashMapUtils;
import com.kuretru.microservices.common.utils.UuidUtils;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
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

    private void saveList(List<LedgerEntryDetailDTO> records) {
        records.forEach(record -> {
            LedgerEntryDetailDTO newRecord = super.save(record);

            if (record.getUserId().equals(AccessTokenContext.getUserId())) {
                record.getTags().forEach(tag -> tag.setEntryDetailId(newRecord.getId()));
                entryTagService.save(record.getId(), record.getTags());
            }
        });
    }

    @Override
    public List<LedgerEntryDetailDTO> save(UUID entryId, List<LedgerEntryDetailDTO> records) throws ServiceException {
        saveList(records);
        return listByEntryId(entryId);
    }

    private void updateList(List<LedgerEntryDetailDTO> records) {
        records.forEach(record -> {
            LedgerEntryDetailDTO newRecord = super.update(record);

            if (record.getUserId().equals(AccessTokenContext.getUserId())) {
                record.getTags().forEach(tag -> tag.setEntryDetailId(newRecord.getId()));
                entryTagService.update(record.getId(), record.getTags());
            }
        });
    }

    @Override
    public List<LedgerEntryDetailDTO> update(UUID entryId, List<LedgerEntryDetailDTO> records) throws ServiceException {
        List<LedgerEntryDetailDTO> oldRecords = listByEntryId(entryId);
        Map<UUID, LedgerEntryDetailDTO> oldRecordsMap = new HashMap<>(HashMapUtils.initialCapacity(oldRecords.size()));
        Map<UUID, LedgerEntryDetailDTO> oldUserRecordsMap = new HashMap<>(HashMapUtils.initialCapacity(oldRecords.size()));
        oldRecords.forEach(detail -> {
            oldRecordsMap.put(detail.getId(), detail);
            oldUserRecordsMap.put(detail.getUserId(), detail);
        });

        List<LedgerEntryDetailDTO> addList = new ArrayList<>();
        List<LedgerEntryDetailDTO> updateList = new ArrayList<>();
        List<UUID> removeList = new ArrayList<>();
        records.forEach(record -> {
            if (UuidUtils.isEmpty(record.getId())) {
                // 新增的记录
                if (oldUserRecordsMap.containsKey(record.getUserId())) {
                    throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该明细下已存在该用户");
                }
                addList.add(record);
            } else if (oldRecordsMap.containsKey(record.getId())) {
                // 更新记录
                LedgerEntryDetailDTO old = oldRecordsMap.get(record.getId());
                oldRecordsMap.remove(record.getId());
                if (!old.getEntryId().equals(record.getEntryId())) {
                    throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不能修改条目ID");
                } else if (!old.getUserId().equals(record.getUserId())) {
                    throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不能修改用户ID");
                }
                updateList.add(record);
            } else {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不属于该条目的明细ID");
            }
        });
        oldRecordsMap.forEach((id, record) -> removeList.add(id));

        saveList(addList);
        updateList(updateList);
        removeByUuidList(removeList);

        return listByEntryId(entryId);
    }

    private void removeByUuidList(List<UUID> uuidList) {
        List<String> ids = uuidList.stream().map(UUID::toString).toList();
        QueryWrapper<LedgerEntryDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("uuid", ids);
        mapper.delete(queryWrapper);
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
