package com.kuretru.web.libra.tools.service.impl;

import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.tools.entity.transfer.CalculatorDTO;
import com.kuretru.web.libra.tools.entity.transfer.CalculatorResult;
import com.kuretru.web.libra.tools.service.CalculatorService;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public CalculatorResult add(CalculatorDTO record) {
        return new CalculatorResult(record.getX().add(record.getY()));
    }

    @Override
    public CalculatorResult subtract(CalculatorDTO record) {
        return new CalculatorResult(record.getX().subtract(record.getY()));
    }

    @Override
    public CalculatorResult multiply(CalculatorDTO record) {
        return new CalculatorResult(record.getX().multiply(record.getY()));
    }

    @Override
    public CalculatorResult divide(CalculatorDTO record) throws ServiceException {
        if (record.getAccuracy() == 0) {
            throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "未提供精度字段");
        }
        return new CalculatorResult(record.getX().divide(record.getY(), record.getAccuracy(), RoundingMode.HALF_DOWN));
    }

}
