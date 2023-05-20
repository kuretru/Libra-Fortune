package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.data.PaymentChannelDO;
import com.kuretru.web.libra.entity.transfer.PaymentChannelDTO;
import com.kuretru.web.libra.entity.view.PaymentChannelVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface PaymentChannelEntityMapper extends BaseEntityMapper<PaymentChannelDO, PaymentChannelDTO> {

    /**
     * 将DO转换为VO
     *
     * @param record DO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    PaymentChannelVO doToVo(PaymentChannelDO record);

    /**
     * 将DO批量转换为VO
     *
     * @param records DO列表
     * @return VO列表
     */
    List<PaymentChannelVO> doToVo(List<PaymentChannelDO> records);

}
