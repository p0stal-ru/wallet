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
import java.util.UUID;

import static com.example.wallet_service.enums.OperationType.WITHDRAW;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

//    @Transactional
//    public void processTransaction(WalletRequest walletRequest) {
//
//        Wallet wallet = walletRepository.findById(walletRequest.getWalletId())
//                .orElseThrow(WalletNotFoundException::new);
//
//        BigDecimal currentBalance = wallet.getBalance();
//
//        OperationType operationType = OperationType.valueOf(walletRequest.getOperationType());
//
//
//        BigDecimal newOperationBalance = switch (operationType) {
//            case DEPOSIT -> walletRequest.getAmount();
//            case WITHDRAW -> validateWithdraw(currentBalance, walletRequest.getAmount(), operationType);
//        };
//
//
//        BigDecimal resultBalance = currentBalance.add(newOperationBalance);
//
//        wallet.setBalance(resultBalance);
//        walletRepository.save(wallet);
//    }

    @Transactional
    public void processTransaction(WalletRequest walletRequest) {

        Wallet wallet = walletRepository.findById(walletRequest.getWalletId())
                .orElseThrow(WalletNotFoundException::new);


        OperationType operationType = OperationType.valueOf(walletRequest.getOperationType());

        synchronized (wallet) {
            switch (operationType) {
                case DEPOSIT:
                    wallet.setBalance(wallet.getBalance().add(walletRequest.getAmount()));
                    walletRepository.save(wallet);
                    log.info("Поступление на счет: {}", walletRequest);
                    break;

                case WITHDRAW:
                    if (wallet.getBalance().compareTo(walletRequest.getAmount()) < 0) {
                        throw new InsufficientFundsException();
                    }
                    wallet.setBalance(wallet.getBalance().subtract(walletRequest.getAmount()));
                    walletRepository.save(wallet);
                    log.info("Списание: {}", wallet);
                    break;

                default:
                    throw new IllegalArgumentException("Не корректно введена операция");
            }

        }
    }


    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);
        log.info("Баланс: {}", wallet);
        return wallet.getBalance();
    }


    private BigDecimal validateWithdraw(BigDecimal currentBalance, BigDecimal newBalance, OperationType operationType) {
        if (currentBalance.compareTo(newBalance) < 0 && operationType == WITHDRAW) {
            throw new InsufficientFundsException();
        }

        return newBalance;
    }

}
