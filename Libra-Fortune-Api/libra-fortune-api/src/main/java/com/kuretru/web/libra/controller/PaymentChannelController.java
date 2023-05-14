package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.PaymentChannelQuery;
import com.kuretru.web.libra.entity.transfer.PaymentChannelDTO;
import com.kuretru.web.libra.service.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@RestController
@RequestMapping("/payments/channels")
@RequireAuthorization
public class PaymentChannelController
        extends BaseRestController<PaymentChannelService, PaymentChannelDTO, PaymentChannelQuery> {

    @Autowired
    public PaymentChannelController(PaymentChannelService service) {
        super(service);
    }

}
