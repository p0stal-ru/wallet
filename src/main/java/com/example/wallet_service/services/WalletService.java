package com.example.wallet_service.services;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.enums.OperationType;
import com.example.wallet_service.exceptions.InsufficientFundsException;
import com.example.wallet_service.exceptions.WalletNotFoundException;
import com.example.wallet_service.models.Wallet;
import com.example.wallet_service.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.wallet_service.enums.OperationType.WITHDRAW;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public void processTransaction(WalletRequest walletRequest) {

        Wallet wallet = walletRepository.findById(walletRequest.getWalletId())
                .orElseThrow(WalletNotFoundException::new);

        BigDecimal currentBalance = wallet.getBalance();

        OperationType operationType = OperationType.valueOf(walletRequest.getOperationType());


        BigDecimal newOperationBalance = switch (operationType) {
            case DEPOSIT -> walletRequest.getAmount();
            case WITHDRAW -> validateWithdraw(currentBalance, walletRequest.getAmount(), operationType);
        };


        BigDecimal resultBalance = currentBalance.add(newOperationBalance);

        wallet.setBalance(resultBalance);
        walletRepository.save(wallet);
    }


    private BigDecimal validateWithdraw(BigDecimal currentBalance, BigDecimal newBalance, OperationType operationType) {
        if (currentBalance.compareTo(newBalance) < 0 && operationType == WITHDRAW) {
            throw new InsufficientFundsException();
        }

        return newBalance;
    }

}
