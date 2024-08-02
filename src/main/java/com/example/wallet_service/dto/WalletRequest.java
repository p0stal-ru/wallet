package com.example.wallet_service.dto;

import com.example.wallet_service.enums.OperationType;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {

    private UUID walletId;

    private String operationType;

    private BigDecimal amount;
}
