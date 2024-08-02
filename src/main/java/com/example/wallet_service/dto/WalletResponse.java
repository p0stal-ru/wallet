package com.example.wallet_service.dto;

import com.example.wallet_service.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "Сумма должна не может быть отрицательной")
    @NotNull
    private BigDecimal balance;

}
