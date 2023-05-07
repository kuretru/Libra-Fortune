package com.kuretru.web.libra.entity.mapper;

import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@SpringBootTest
class LedgerEntityMapperTest {

    private final LedgerEntityMapper entityMapper;

    @Autowired
    public LedgerEntityMapperTest(LedgerEntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }

    @Test
    void doToDto() {
        LedgerDO ledgerDO = new LedgerDO();
        ledgerDO.setUuid(UUID.randomUUID().toString());
        ledgerDO.setOwnerId(UUID.randomUUID().toString());
        ledgerDO.setName("name");
        ledgerDO.setType((short)1);
        ledgerDO.setRemark("remark");

        LedgerDTO ledgerDTO = entityMapper.doToDto(ledgerDO);

        assertEquals(ledgerDO.getUuid(), ledgerDTO.getId().toString());
        assertEquals(ledgerDO.getOwnerId(), ledgerDTO.getOwnerId().toString());
        assertEquals("name", ledgerDTO.getName());
        assertEquals("remark", ledgerDTO.getRemark());
        assertEquals(LedgerTypeEnum.CO_COMMON, ledgerDTO.getType());
    }

    @Test
    void dtoToDo() {
        LedgerDTO ledgerDTO = new LedgerDTO();
        ledgerDTO.setId(UUID.randomUUID());
        ledgerDTO.setOwnerId(UUID.randomUUID());
        ledgerDTO.setName("name");
        ledgerDTO.setRemark("remark");
        ledgerDTO.setType(LedgerTypeEnum.COMMON);

        LedgerDO ledgerDO = entityMapper.dtoToDo(ledgerDTO);

        assertEquals(ledgerDTO.getId(), UUID.fromString(ledgerDO.getUuid()));
        assertEquals(ledgerDTO.getOwnerId(), UUID.fromString(ledgerDO.getOwnerId()));
        assertEquals("name", ledgerDO.getName());
        assertEquals("remark", ledgerDO.getRemark());
        assertEquals((short)0, ledgerDO.getType());
    }


}
