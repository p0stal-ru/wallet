package com.example.wallet_service.dto;

import com.example.wallet_service.enums.OperationType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletResponse {

    private UUID walletId;

    private BigDecimal balance;

}
