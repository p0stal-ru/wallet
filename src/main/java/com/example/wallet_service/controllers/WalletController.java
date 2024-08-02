package com.example.wallet_service.controllers;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void makeADeposit(@RequestBody WalletRequest walletRequest) {
        walletService.processTransaction(walletRequest);
    }

//    @GetMapping("/{walletId}")
//    @ResponseStatus(HttpStatus.OK)
//    public WalletResponse getBalance(@PathVariable(name = "walletId") UUID walletId) {
//        BigDecimal balance = walletService.getBalance(walletId);
//        return new WalletResponse(walletId, balance);
//    }
}
