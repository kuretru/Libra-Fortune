package com.kuretru.web.libra.tools.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.tools.entity.transfer.CalculatorDTO;
import com.kuretru.web.libra.tools.entity.transfer.CalculatorResult;

public interface CalculatorService {

    CalculatorResult add(CalculatorDTO record);

    CalculatorResult subtract(CalculatorDTO record);

    CalculatorResult multiply(CalculatorDTO record);

    CalculatorResult divide(CalculatorDTO record) throws ServiceException;

}
