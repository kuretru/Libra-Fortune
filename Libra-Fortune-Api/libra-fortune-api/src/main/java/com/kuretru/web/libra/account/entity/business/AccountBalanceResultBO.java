package com.kuretru.web.libra.account.entity.business;

import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AccountBalanceResultBO {

    @Schema(description = "账户列表")
    private List<AccountDTO> accounts;

    @Schema(description = "By日余额列表")
    private List<AccountBalanceDateBO> balances;

}
