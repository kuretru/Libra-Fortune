package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.business.LedgerMemberBO;
import com.kuretru.web.libra.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.entity.view.LedgerMemberVO;
import com.kuretru.web.libra.entity.view.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface LedgerMemberEntityMapper extends BaseEntityMapper<LedgerMemberDO, LedgerMemberDTO> {

    /**
     * 将BO转换为VO
     *
     * @param record BO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    LedgerMemberVO boToVo(LedgerMemberBO record);

    /**
     * 将BO批量转换为VO
     *
     * @param records BO列表
     * @return VO列表
     */
    List<LedgerMemberVO> boToVo(List<LedgerMemberBO> records);

    /**
     * 将BO转换为VO
     *
     * @param record BO
     * @return VO
     */
    @Mapping(source = "userId", target = "id")
    UserVO boToUserVo(LedgerMemberBO record);

    /**
     * 将BO批量转换为VO
     *
     * @param records BO列表
     * @return VO列表
     */
    List<UserVO> boToUserVo(List<LedgerMemberBO> records);

}
