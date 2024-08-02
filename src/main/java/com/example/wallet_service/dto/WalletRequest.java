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
@Builder
public class WalletRequest {

    //    @NotEmpty(message = "id не должен быть пустым")
    private UUID walletId;

    //    @NotEmpty(message = "Выберите DEPOSIT для пополнения кошелька или WITHDRAW для снятия средств")
    private String operationType;

    @Positive(message = "Сумма должна не может быть отрицательной")
    @NotNull
    private BigDecimal amount;
}
